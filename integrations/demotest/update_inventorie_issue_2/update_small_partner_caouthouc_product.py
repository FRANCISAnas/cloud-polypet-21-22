import json

import requests

from addresses import backend_host

product_path = backend_host + '/product/partner/'
product_to_update = json.loads(
    '{"name":"Caoutchouc","category":"Caniche","price":300,"origin":"ZooPlus","reference":"ZooPlus_base_name_Caoutchouc"}')


def print_json(p):
    print(json.dumps(p, indent=2))


def update_product():
    print('PUT Url', product_path)
    print('Object to update')
    print_json(product_to_update)
    product_update_res = requests.put(product_path,
                                      json=product_to_update)
    assert product_update_res.status_code == 200
    print("Update status code:", product_update_res.status_code)
    print("Update content:")
    print_json(product_update_res.json())
