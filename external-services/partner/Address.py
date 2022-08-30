#!/usr/bin/env python
""" generated source for module Address """

from barnum import gen_data
from random import randint

class Address(object):
    """ generated source for class Address """

    def __init__(self,roadName=None,buildingNumber=None,zipCode=None,country=None,min_building_number=0, max_building_number=10):
        self.roadName = roadName if roadName else gen_data.create_street()
        self.buildingNumber = buildingNumber if buildingNumber else randint(min_building_number,max_building_number)
        self.zipCode = zipCode if zipCode else "".join(gen_data.create_city_state_zip())
        self.country = country if country else "country of "+self.roadName

    def __json__(self):
        return {"roadName": self.roadName, "buildingNumber": self.buildingNumber,
         "zipCode": self.zipCode, "country": self.country}

    def __repr__(self):
        return str(self.__json__())

    def getRoadName(self):
        """ generated source for method getRoadName """
        return self.roadName

    def setRoadName(self, roadName):
        """ generated source for method setRoadName """
        self.roadName = roadName

    def getBuildingNumber(self):
        """ generated source for method getBuildingNumber """
        return self.buildingNumber

    def setBuildingNumber(self, buildingNumber):
        """ generated source for method setBuildingNumber """
        self.buildingNumber = buildingNumber

    def getZipCode(self):
        """ generated source for method getZipCode """
        return self.zipCode

    def setZipCode(self, zipCode):
        """ generated source for method setZipCode """
        self.zipCode = zipCode

    def getCountry(self):
        """ generated source for method getCountry """
        return self.country

    def setCountry(self, country):
        """ generated source for method setCountry """
        self.country = country

