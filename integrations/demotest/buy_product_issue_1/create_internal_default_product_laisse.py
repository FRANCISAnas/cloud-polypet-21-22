import os, json, requests
from addresses import partner_host,backend_host


product_path = backend_host + '/product/internal/'
product_to_add = json.loads('{"name":"Laisse","category":"Caniche","price":400,"reference":"JolyPet_base_name_Laisse"}')
DEFAULT_STORED_QUANTITY=1
def create_product(store_id):
    print('POST Url',product_path)
    print('Object to add', product_to_add)
    product_create_res = requests.post(product_path,
                                      json=product_to_add)
    assert product_create_res.status_code==200
    print("Create status code:", product_create_res.status_code)
    product_create=product_create_res.json()
    print("Create content:", product_create)
    print(f"Storing 1 quantity of {product_create['name']}")
    product_stored_res = requests.get(f"{backend_host}/product/{product_create['reference']}/internal/store/{store_id}/{DEFAULT_STORED_QUANTITY}")
    assert product_stored_res.status_code==200
    print("Stored status code:", product_stored_res.status_code)
