#!/usr/bin/env python
""" generated source for module StoreHouse """

from barnum import gen_data
from random import randint

from Address import Address
from Product import Product
from ProductInventory import ProductInventory


class StoreHouse(object):
    """ generated source for class StoreHouse """

    def __init__(self, name=None, address=None, productInventories=None):
        self.id = id(self)
        self.name = name if name else gen_data.create_sentence().split(" ")[0]
        self.address = address if address else Address()
        self.productInventories = productInventories if productInventories else []

    def __json__(self):
        return {"name": self.name,
                "address": self.address.__json__(),
                "productInventories": [pI.__json__() for pI in self.productInventories]}

    def store_product(self, product: Product, quantity=None, quantity_min=1, quantity_max=10):
        if not quantity: quantity = randint(quantity_min, quantity_max)
        product.store_at(self)
        self.productInventories.append(ProductInventory(product, quantity))
        print(product.name," stored at ",self.name, ", Address",self.address)

    def getAddress(self):
        """ generated source for method getAddress """
        return self.address

    def getId(self):
        """ generated source for method getId """
        return self.id

    def setId(self, id):
        """ generated source for method setId """
        self.id = id

    def getName(self):
        """ generated source for method getName """
        return self.name

    def setName(self, name):
        """ generated source for method setName """
        self.name = name

    def setAddress(self, address):
        """ generated source for method setAddress """
        self.address = address

    def getProductInventories(self):
        """ generated source for method getProductInventories """
        return self.productInventories

    def setProductInventories(self, productInventories):
        """ generated source for method setProductInventories """
        self.productInventories = productInventories


    def incrementProductQuantity(self, product, quantity):
        return self.set_product_quantity(product, quantity)

    def decrementProductQuantity(self, product, quantity):
        return self.set_product_quantity(product, -quantity)

    def get_product_inventory_by_reference(self,reference):
        for productInventory in self.productInventories:
            if str(productInventory.getReference()) ==str(reference):
                return productInventory
        return None

    def get_product_quantity(self,reference):
        product_inventory = self.get_product_inventory_by_reference(reference)
        if product_inventory:
            return product_inventory.getQuantity()
        return -1

    def set_product_quantity(self, product, quantity):

        if not self.productInventories:
            productInventories = [ProductInventory(product, max(quantity, 0))]
            return quantity
        product_inventory = self.get_product_inventory_by_reference(product.getProductReference())
        if product_inventory:
            q = product_inventory.getQuantity()
            product_inventory.setQuantity(max(int(q) + int(quantity), 0))
            return product_inventory.getQuantity()
        else:
            self.productInventories.append(ProductInventory(product, max(quantity, 0)))
        return -1
