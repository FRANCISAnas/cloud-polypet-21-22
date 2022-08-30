from .create_internal_default_product_laisse import create_product as create_internal_product, backend_host
from .create_small_partner_caouthouc_product import create_product as create_small_partner_product
from .create_big_partner_medium_maxi_adult_product import create_product as create_big_partner_product,partner_host

import requests

def initialize():

    print("*********\tGetting or create store for internal products\t*********")
    store_id = getting_or_creating_store()

    print("*********\tInitializing Internal Default Product Laisse\t*********")
    create_internal_product(store_id)
    print("*********\tInitializing Small Partner Default Product Caoutchouc\t*********")
    create_small_partner_product(store_id)
    print("*********\tInitializing Big Partner Default Product Medium Maxi Adult\t*********")
    #Subscribing of partner to catalogue
    subscribe_partner(partner_host)
    #subscribe_partner("https://dry-forest-88408.herokuapp.com")
    create_big_partner_product()

def getting_or_creating_store():
    print(f"Getting available store")
    getting_store_res = requests.get(backend_host + '/store/available')
    assert getting_store_res.status_code == 200
    store_id=getting_store_res.content.decode()
    print(f"Store id gotten {store_id}.")
    if str(store_id)!=str(-1): 
        return store_id
    print(f"Creating store")
    store_created_res = requests.post(backend_host + "/store/Pet'sWorld",json={
        "roadName":"4 Simon Veil",
        "buildingNumber":"5",
        "zipCode":"16689WaterfallPA",
        "country":"country of 4 Simon Veil",
    })
    assert store_created_res.status_code == 200
    store_id=store_created_res.content.decode()
    assert str(store_id)!=str(-1)
    print(f"Created store Pet'sWorld with success, id gotten {store_id}.")
    return store_id

def subscribe_partner(partner_host):
    print(f"Subscribing of partner {partner_host} to catalogue {backend_host}.")
    partner_subscribing_res = requests.post(partner_host + '/j/partner/JolyPet/subscribe',
                                            data=backend_host)
    assert partner_subscribing_res.status_code == 200
    print(f"Subscribe of partner {partner_host} to catalogue {backend_host} with success.")
