#!/usr/bin/env python

class ProductInventory(object):
    """ generated source for class ProductInventory """

    def __init__(self,product,quantity):
        self.id = id(self)
        self.product = product
        self.quantity = quantity

    def __json__(self):
        return {"product":self.product.__json__(),"quantity":self.quantity}

    def getId(self):
        """ generated source for method getId """
        return self.id

    def setId(self, id):
        """ generated source for method setId """
        self.id = id

    def getReference(self):
        return self.product.getProductReference()

    def getProduct(self):
        """ generated source for method getProduct """
        return self.product

    def setProduct(self, product):
        """ generated source for method setProduct """
        self.product = product

    def getQuantity(self):
        """ generated source for method getQuantity """
        return self.quantity

    def setQuantity(self, quantity):
        """ generated source for method setQuantity """
        self.quantity = quantity

