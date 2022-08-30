import os, json, requests
from dotenv import load_dotenv
from addresses import partner_host,backend_host,customer_care
from initialize import initialize
load_dotenv()

subscribe = customer_care + '/customer'
add_order_line = customer_care + '/add/'
validate_and_pay = customer_care + '/confirm/'
search_by_name = customer_care + '/products/'
search_by_category_path = customer_care + '/products/category/'
products = []


def print_json(p):
    print(json.dumps(p, indent=2))


def get_product_quantity(product_reference):
    product_quantity_res = requests.get(f"{backend_host}/product/{product_reference}/quantity")

    assert product_quantity_res.status_code == 200

    return int(product_quantity_res.content.decode())


def add_product_to_cart(product_name, products_fetched, token, quantity=1):
    product_in_order_line = None
    for p in products_fetched:
        if str(p["name"]) == str(product_name):
            product_in_order_line = p
            break
    order_line = {
        'product': product_in_order_line,
        'quantity': quantity
    }
    print("Adding orderline")
    print_json(order_line)

    product_order_line_res = requests.post(add_order_line + token, json=order_line)

    assert product_order_line_res.status_code == 200

    print("Order line added to cart!")
    return product_in_order_line["reference"], quantity, get_product_quantity(product_in_order_line["reference"])


def check_quantity_after_purchase(product_reference, before_bought_quantity, bought_quantity):
    current_quantity = get_product_quantity(product_reference)

    assert current_quantity <= before_bought_quantity - bought_quantity

    print(f"{bought_quantity} product" + (
        "s were " if bought_quantity > 1 else " was ") + f"taken from {product_reference}'s quantity, Current available quantity {current_quantity}.")


def main():
    initialize()

    # Subscribe/login
    customer = {
        'name': "Phillipe",
        'email': "phillipe@etu.unice.fr"
    }

    print(f"Subsribing {customer} ...")

    customer_subscribe_res = requests.post(subscribe, json=customer)
    assert customer_subscribe_res.status_code == 200

    print("customer registered!")

    token = customer_subscribe_res.content.decode()

    # Search products
    print("Searching products...")
    print("Search of Caniche category!")
    products_caniche_res = requests.get(search_by_category_path + 'Caniche')
    assert products_caniche_res.status_code == 200

    products_caniche = products_caniche_res.json()

    print("Caniche category found with " + str(len(products_caniche)) + " products!")

    products_caniches_name = [p["name"] for p in products_caniche]

    for p in products_caniche: products.append(p)

    assert 'Laisse' in products_caniches_name
    assert 'Caoutchouc' in products_caniches_name

    print("Result contains Laisse and Caoutchouc!")

    print("Search of Medium Maxi")
    products_croquette_res = requests.get(search_by_name + 'Medium Maxi')

    assert products_croquette_res.status_code == 200
    products_croquette = products_croquette_res.json()

    print("Found " + str(len(products_croquette)) + " product(s) containing Medium Maxi!")

    products_croquettes_name = [p["name"] for p in products_croquette]

    for p in products_croquette: products.append(p)

    assert 'Medium Maxi Adult' in products_croquettes_name

    print('Medium Maxi Adult is in found products')

    # Add products in the cart
    added_laisse_reference, added_laisse_quantity, added_laisse_available_quantity = add_product_to_cart('Laisse',
                                                                                                         products_caniche,
                                                                                                         token)
    added_caoutchouc_reference, added_caoutchouc_quantity, added_caoutchouc_available_quantity = add_product_to_cart(
        'Caoutchouc', products_caniche, token)
    added_medium_maxi_adult_reference, added_medium_maxi_adult_quantity, added_medium_maxi_adult_abvailable_quantity = add_product_to_cart(
        'Medium Maxi Adult', products_croquette, token)

    # validate and pay cart
    print("Validating and buying...")
    payment_line_res = requests.post(validate_and_pay + token, json={
        'creditCard': {
            'cardNumber': '76898768976',
            'ccv': '827892870',
            'expirationDate': '678978678'},
        'destination': {
            'roadName': 'St ALbert',
            'buildingNumber': 1342,
            'zipCode': '345893',
            'country': 'The country of 345893'}
    })

    assert payment_line_res.status_code == 200

    print(f"Cart bought successfully. response content: ")
    print_json(payment_line_res.json())

    # test quantity
    check_quantity_after_purchase(added_laisse_reference, added_medium_maxi_adult_abvailable_quantity,
                                  added_laisse_quantity)
    check_quantity_after_purchase(added_caoutchouc_reference, added_caoutchouc_available_quantity,
                                  added_caoutchouc_quantity)
    check_quantity_after_purchase(added_medium_maxi_adult_reference, added_medium_maxi_adult_abvailable_quantity,
                                  added_medium_maxi_adult_quantity)

    return [added_laisse_reference, added_caoutchouc_reference, added_medium_maxi_adult_reference]


if __name__ == '__main__':
    main()
