from addresses import partner_host,backend_host,partner_name
from update_internal_default_product_laisse import update_product as update_internal_product, backend_host
from update_small_partner_caouthouc_product import update_product as update_small_partner_product
from update_big_partner_medium_maxi_adult_product import update_product as update_big_partner_product
import os,requests




partner_subscribe_path = backend_host + f'/partner/{partner_name}/subscribe'


def subscribe_partner(partner_host):
    print(f"Subscribing of partner {partner_host} to catalogue {backend_host}.")
    partner_subscribing_res = requests.post(partner_host + '/j/partner/JolyPet/subscribe',
                                            data=backend_host)
    assert partner_subscribing_res.status_code == 200
    print(f"Subscribe of partner {partner_host} to catalogue {backend_host} with success.")

if __name__ == '__main__':
    print("*********\tUpdating Internal Default Product Laisse\t*********")
    update_internal_product()
    print("*********\tUpdating Small Partner Default Product Caoutchouc\t*********")
    update_small_partner_product()
    print("*********\tUpdating Big Partner Default Product Medium Maxi Adult\t*********")
    #Subscribing of partner to catalogue
    subscribe_partner(partner_host)
    update_big_partner_product()

