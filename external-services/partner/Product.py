#!/usr/bin/env python
""" generated source for module Product """
from typing import Dict, Any

from barnum import gen_data
from random import randint

#from StoreHouse import StoreHouse


class Product(object):
    """ generated source for class Product """
    def __repr__(self) -> str:
        return str(self.__json__())

    def __init__(self,origin,price=None,min_price=60,max_price=380,name = None,category = None):
        self.store_house = None
        self.id = id(self)
        sentence_splitted = gen_data.create_sentence().split(" ")
        self.origin = origin
        self.price = price if price else randint(min_price, max_price)
        self.name = name if name else sentence_splitted[0]
        self.category = category if category else sentence_splitted[1]
        self.reference = self.origin + '_' + ('base_name_'+name if name else str(self.id))
        self.reference = self.reference.replace(' ','_')

    def __json__(self):
        return {"name":self.name,"category":self.category,"origin":self.origin,"price":self.price,"reference":self.reference,"store_id":self.store_house.id if self.store_house else -1}

    def getProductReference(self):
        return self.reference

    def store_at(self,store_house):
        self.store_house = store_house

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

    def getCategory(self):
        """ generated source for method getCategory """
        return self.category

    def setCategory(self, category):
        """ generated source for method setCategory """
        self.category = category

    def getOrigin(self):
        """ generated source for method getOrigin """
        return self.origin

    def setOrigin(self, origin):
        """ generated source for method setOrigin """
        self.origin = origin

    def getPrice(self):
        """ generated source for method getPrice """
        return self.price

    def setPrice(self, price):
        """ generated source for method setPrice """
        self.price = price

    def get_store_house(self):
        return self.store_house

