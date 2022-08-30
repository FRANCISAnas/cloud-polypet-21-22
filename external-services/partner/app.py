from _ast import stmt
from builtins import complex

from flask import Flask, request, url_for
import os, threading, requests
from Product import Product
import json

from barnum import gen_data
from time import sleep
from StoreHouse import StoreHouse

from dotenv import load_dotenv

load_dotenv()
product = json.loads(os.getenv("PRODUCT")) if os.getenv("PRODUCT", None) != None else None
if product:
    if not ("name" in product) or not ("category" in product) or not ("price" in product):
        product = None
        print("The product object can't be add, you must add these properties: ")
        if not ("name" in product): print("name")
        if not ("category" in product): print("category")
        if not ("price" in product): print("price")

max_products = int(os.getenv("MAX_PRODUCTS", 10))
products_number_per_ware_house = int(os.getenv("PRODUCTS_NUMBER_PER_WARE_HOUSE", 2))

max_store_houses = int(os.getenv("MAX_STORE_HOUSES", max_products // products_number_per_ware_house))

company_name = os.getenv("COMPANY_NAME", gen_data.create_company_name())
main_partner_url = os.getenv("MAIN_PARTNER_URL", "localhost:8081/")

products = [Product(company_name) for _ in range(max_products - 1 if product else max_products)]
ware_houses = [StoreHouse() for _ in range(max_store_houses)]

main_exchange_name = "productsinventory"
main_routing_key = "product.update"

PARNTER_SUBSCRIPTION_WAITING_MAX_SECOND = 8192

partner_subcription_loop = {}
current_url = os.getenv("CURRENT_URL", "https://archicloud-j-partner-ultima.herokuapp.com")

print("MAX_STORE_HOUSES", max_store_houses)

if product:
    print("Adding product: ", product)
    products.append(Product(company_name, name=product["name"], category=product["category"], price=product["price"]))

for i in range(max_store_houses):
    for j in range(i * products_number_per_ware_house,
                   i * products_number_per_ware_house + products_number_per_ware_house):
        ware_houses[i].store_product(products[j])
    # print("Warehouse To Json", json.dumps(ware_houses[i]))
print("Company name: ", company_name, "\nBegin with total product:", len(products))

connection = None
channel = None


def send_products_to_partner(partner_url):
    global product, produc_json
    products_json = [p.__json__() for p in products]
    create_sent = False
    for i in range(5):
        try:
            create_product_res = requests.post(f"{partner_url}/products/partner/", json=products_json)

            if create_product_res.status_code != 200: raise Exception('Oops something went wrong!')

            print(" [x] Sent to partner: %r product(s) %r." % (
                len(products_json), products_json))

            create_sent = True
            return True
        except:
            print('Request Failed, retrying (', 5 - i, ') ...')

    if not create_sent: print(
        f"Impossible to add product to partner {main_partner_url}. Please Ask him to add them manually.")
    return False


app = Flask(__name__)


def has_no_empty_params(rule):
    defaults = rule.defaults if rule.defaults is not None else ()
    arguments = rule.arguments if rule.arguments is not None else ()
    return len(defaults) >= len(arguments)


@app.route("/site-map")
def site_map():
    links = []
    for rule in app.url_map.iter_rules():
        # Filter out rules we can't navigate to in a browser
        # and rules that require parameters
        url = url_for(rule.endpoint, **(rule.defaults or {}))
        links.append(url)
    return links
    # links is now a list of url, endpoint tuples


@app.route('/', methods=["GET"])
def home():
    # routes = ''
    # print(site_map())
    # for route in site_map(): routes += '\n ' + route
    # \nAll available routes {routes}
    return f"WELCOME PARTNER {company_name} PAGE!:-)"

@app.route('/status', methods=["GET"])
def status():
    return 'SUCCESS'

@app.route('/j/partner/<partner_name>/stop_loop', methods=["GET"])
def stop_subscribing_loop(partner_name):
    partner_subcription_loop[partner_name]['state'] = False
    return f"Subscribing loop to partner {partner_name} stopped! :-)"


@app.route('/j/product/<product_reference>', methods=["GET"])
def get_product(product_reference):
    for product in products:
        if product.getProductReference() == product_reference:
            return product.__json__()
    return {}


@app.route('/j/product/first/name/<product_name>', methods=["GET"])
def get_product_by_name(product_name):
    for product in products:
        if product.getName() == product_name:
            return product.__json__()
    return {}


@app.route('/j/product/<product_reference>', methods=["PUT"])
def update_product(product_reference):
    product = find_product(product_reference)
    if product:
        update_product = request.json
        product.setName(update_product["name"])
        product.setCategory(update_product["category"])
        product.setPrice(update_product["price"])
        send_update_in_bus(product)
        return product.__json__()
    return {}

@app.route('/j/product/<reference>/quantity', methods=["GET"])
def get_current_quantity(reference):
    product = find_product(reference)
    if not product:
        print(f"Product with reference {reference} was not found!")
        return json.dumps(-1)
    warehouse = product.get_store_house()
    print(warehouse.__json__())
    quantity = warehouse.get_product_quantity(reference)
    if quantity == -1:
        print(f"Something went wrong with {product.getName()} from the store "
              f"{warehouse.getName()}. Incorrect stored number!")
    return json.dumps(quantity)

@app.route('/j/purchase/product/<reference>/<quantity>', methods=["GET"])
def purchase(reference, quantity):
    product = find_product(reference)
    if not product:
        print(f"Product with reference {reference} was not found!")
        return json.dumps(False)
    warehouse = product.get_store_house()
    newQuantity = warehouse.decrementProductQuantity(product, int(quantity))
    if newQuantity == -1:
        print(f"Something went wrong while decrementing {product.getName()} from the store "
              f"{warehouse.getName()}!")
    return json.dumps(newQuantity != -1)


@app.route('/j/purchase/book/product/<reference>', methods=["GET"])
def can_purchase(reference):
    product = find_product(reference)
    if not product:
        print(f"Product with reference {reference} was not found!")
        return json.dumps(False)
    warehouse = product.get_store_house()
    quantity = warehouse.get_product_quantity(reference)
    return json.dumps(quantity > 0)


@app.route('/j/product/<reference>/store/<store_id>/<quantity>', methods=["GET"])
def store_product(reference, store_id, quantity):
    for ware_house in ware_houses:
        if str(ware_house.getId()) == str(store_id):
            product = find_product(reference)
            if not product:
                print(f"Sorry the product with reference {reference} was not found!")
                return

            newQuantity = ware_house.incrementProductQuantity(product, quantity)

            if newQuantity == -1:
                print(f"Something went wrong while incrementing {product.getName()} from the store "
                      f"{ware_house.getName()}!")
            return json.dumps(newQuantity)
    print(f"Product with reference {reference} was not stored at the store {store_id}.")
    return json.dumps(-1)


@app.route('/j/warehouse/product/address/<product_reference>', methods=["GET"])
def get_product_address(product_reference):
    product = find_product(product_reference)
    return product.get_store_house().getAddress().__json__() if product else {}


def find_product(product_reference):
    for product in products:
        if product.getProductReference() == product_reference:
            return product
    return None


def send_update_in_bus(product):
    update_sent = False
    for i in range(5):
        try:
            print("Sending update via url " + main_partner_url)
            update_product_res = requests.put(f"{main_partner_url}/product/partner/", json=product.__json__())

            print(" [x] Send update for product with product_reference %r and content %r" % (
                product.getProductReference(), update_product_res.json()))
            if update_product_res.status_code != 200: raise Exception('Oops something went wrong!')
            update_sent = True
            break
        except:
            print('Request failed, retrying(', 5 - i, ') ...')

    if not update_sent: print("Impossible to update the product at the partner, it must be update manually.")


@app.route('/j/partner/<partner_name>/subscribe', methods=["POST"])
def partner_subscription_rest_call(partner_name):
    threading.Thread(target=partner_subscription, args=(partner_name, request.data.decode())).start()
    sleep(5)
    return json.dumps(partner_subcription_loop[partner_name] if partner_name else "process subscription")


def partner_subscription(partner_name, partner_url=None):
    global main_partner_url

    main_partner_url = partner_url if partner_url else request.data.decode()

    if partner_name in partner_subcription_loop and partner_subcription_loop[partner_name]['state']:
        print(
            f'Stopping previous subscribing to partner {partner_name}, waiting time {partner_subcription_loop[partner_name]["current_sleeping_time"]}')
        sleep(partner_subcription_loop[partner_name]["current_sleeping_time"])

    partner_subcription_loop[partner_name] = {'state': False, 'current_sleeping_time': 1, 'subscribe': False}

    print('Subscribing to partner ' + main_partner_url)
    partner_subcription_loop[partner_name] = {'state': True, 'current_sleeping_time': 1, 'subscribe': False}

    while not partner_subcription_loop[partner_name]['subscribe'] and partner_subcription_loop[partner_name]["state"]:

        try:

            subscription_res = requests.post(f"{main_partner_url}/partner/{company_name}/subscribe", data=current_url)

            if subscription_res.status_code != 200: raise Exception('Oops something went wrong while subscribing!')
            print(f"Subscribed to partner {partner_name} with success.")
            partner_subcription_loop[partner_name]['subscribe'] = True
            partner_subcription_loop[partner_name]['current_sleeping_time'] = 1
            break
        except:
            print('Subscribing request Failed, retrying ...')

        print(f"Sorry something went wrong while subscribing to partner {partner_name} with url: " + main_partner_url)
        print(f'Waiting for {partner_subcription_loop[partner_name]["current_sleeping_time"]} second(s) before retry!')
        sleep(partner_subcription_loop[partner_name]["current_sleeping_time"])
        partner_subcription_loop[partner_name]["current_sleeping_time"] *= 2 if partner_subcription_loop[partner_name][
                                                                                    "current_sleeping_time"] < PARNTER_SUBSCRIPTION_WAITING_MAX_SECOND else 1

    while not send_products_to_partner(main_partner_url) and partner_subcription_loop[partner_name]["state"]:
        print("Sorry something went wrong while sending products to partner " + partner_url)
        print(f'Waiting for {partner_subcription_loop[partner_name]["current_sleeping_time"]} second(s) before retry!')
        sleep(partner_subcription_loop[partner_name]["current_sleeping_time"])
        partner_subcription_loop[partner_name]["current_sleeping_time"] *= 2 if partner_subcription_loop[partner_name][
                                                                                    "current_sleeping_time"] < PARNTER_SUBSCRIPTION_WAITING_MAX_SECOND else 1

    partner_subcription_loop[partner_name]["state"] = False
    partner_subcription_loop[partner_name]['current_sleeping_time'] = 1
    return json.dumps(True)


@app.route('/j/product', methods=["POST"])
def create_product():
    new_product = request.json
    product = Product(company_name, name=new_product["name"], price=new_product["price"],
                      category=new_product["category"])
    ware_houses[0].store_product(product)
    products.append(product)
    send_update_in_bus(product)
    # print(" [x] Send Create for product with product_reference %r and content %r" % product.getProductReference(),
    #      produc_json)
    return product.__json__()


@app.route('/j/all/about/<product_reference>', methods=["GET"])
def get_all_about_product(product_reference):
    for product in products:
        if product.getProductReference() == product_reference:
            store_house = product.get_store_house()
            store_house = store_house.__json__()
            productInventories = []
            for productInventory in store_house["productInventories"]:
                if productInventory["product"]["name"] == product.getName():
                    productInventories = [productInventory]
                    break
            store_house["productInventories"] = productInventories
            return store_house
    return {}


@app.route('/j/products', methods=["GET"])
def getProduct():
    return products


if __name__ == '__main__':
    host, port = os.getenv('HOST', 'localhost'), int(os.getenv('PORT', 5000))
    partner_name, partner_link = os.getenv('PARTNER_NAME', 'Ultima'), os.getenv('PARTNER_HOST',
                                                                                'http://35.241.189.130:31000')
    threading.Thread(target=partner_subscription, args=(partner_name, partner_link)).start()
    app.run(debug=True, host=host, port=port)
