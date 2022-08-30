import json
import os

import requests

from addresses import partner_host

product_path = partner_host + '/j/product'

created_product = json.loads(os.getenv('PRODUCT','{"name":"Medium Maxi Adult","category":"croquette","price":350}'))


def print_json(p):
    print(json.dumps(p, indent=2))



def update_product():



    creation_url = product_path + '/first/name/' + created_product["name"]
    print("GET Url: ",creation_url)
    product_from_partner_res = requests.get(creation_url)
    assert product_from_partner_res.status_code==200
    print(product_from_partner_res.status_code)
    if product_from_partner_res.status_code == 200:
        product_from_partner = product_from_partner_res.json()
        print_json(product_from_partner)
        product_from_partner["price"] = os.getenv('PRODUCT_NEW_PRICE', 800)
        update_url = product_path + '/' + product_from_partner['reference']
        print('PUT Url',update_url)
        print('Object to update')
        print_json(product_from_partner)
        product_from_partner_res = requests.put(update_url,
                                                json=product_from_partner)
        assert product_from_partner_res.status_code==200
        print("Update status code:", product_from_partner_res.status_code)
        print("Update content:")
        print_json(product_from_partner_res.json())
