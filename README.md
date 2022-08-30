# Polypet

## Déploiement de notre solution sur le cloud

Prérequis: environnement de type Linux (base64, envsubst, curl , tar, ...)

Les liens des services :

http:/34.117.40.226/sitemonitoring/ -> Site Monitoring (statistiques)

http://34.117.40.226/catalog/ -> Catalog Manager

http://34.117.40.226/ -> Customer Care

http://34.117.40.226/accounting/ -> Accounting

Par manque de crédits notre solution pourrait ne plus être accessible.
Pour redéployer il faut :
- Créer un nouveau projet sur GCP
- Créer un serveur de base de données PostgresSQL sur Cloud SQL et récupérer l'addresse IP et le mot de passe
  Créer les bases de données de chaque service en lancant les commandes suivantes (changer <polypet-db-instance> par le nom de la base de données)

```sh
gcloud sql databases create customercaredb --instance=polypet-db-instance --charset=UTF8 --collation=en_US.UTF8
gcloud sql databases create cloudpolypetsitemonitoringdb --instance=polypet-db-instance --charset=UTF8 --collation=en_US.UTF8
gcloud sql databases create catalogdb --instance=polypet-db-instance --charset=UTF8 --collation=en_US.UTF8
gcloud sql databases create cloudpolypetaccountingdb --instance=polypet-db-instance --charset=UTF8 --collation=en_US.UTF8
```
- Mettre à jour les secrets  POSTGRES_SERVER_PWD, POSTGRES_SERVER_URL et GOOGLE_CLOUD_CREDENTIALS dans Github
  si vous voulez faire le déploiement en utilisant Github Actions.
  Si vous voulez déployer depuis votre PC, les déclarer en tant que variables d'environnement puis lancer les commandes
```sh
bash deploy.images.sh
bash update.kubernetes.sh
```

**Note**: pour GOOGLE_CLOUD_CREDENTIALS
Pour obtenir les credentials allez sur https://console.cloud.google.com/iam-admin/serviceaccounts?project=<nom-du-projet>
et créer un nouveau service account. Enregistrez les credentials et encodez les avec
`base64 --wrap=0 <nom-du-fichier-de-credentials>`
récupérer la valeur encodée et l'utiliser comme valeur de la variable d'environnement | secrets GOOGLE_CLOUD_CREDENTIALS.



## Les scénarios supportés
### Scénario 1

- En tant que client de PolyPet, je veux voir les produits triés dans des catalogues,
afin de trouver le produit qui m’intéresse.


- En tant que cliente qui a ajouté des produits au panier je veux procéder à l’achat en utilisant ma carte bancaire,
afin de finaliser l’achat


- En tant que client de PolyPet ayant passé une commande sur le site internet, je veux que les produits de ma commande soient livrés par drone, 
afin de clôturer mon achat.


```sh
cd integrations/demotest/buy_product_issue_1
python3 main.py
```

### Scénario 2

- En tant que manager d’une industrie pharmaceutique, je voudrais ajouter mes produits sur le site de PolyPet 
afin que les clients du site puissent voir mon produit


- En tant que manager je souhaite gérer les produits sur les sites,
afin de mettre à jour le site avec les nouveaux produits.


```sh
cd integrations/demotest/update_inventorie_issue_2
python3 main.py
```


### Scénario 3

- En tant que membre de l’équipe Marketing de PolyPet je souhaite voir les statistiques des visites et les achats sur les différentes pages des produits
afin de mettre en place une nouvelle stratégie de vente.

```sh
cd integrations/demotest/consult_statistique_issue_3
python3 main.py
``` 

### Liens de la banque et d'un partenaire de Polypet
 ont été déployés sur Heroku
- Banque:  "https://archicloud-j-bank.herokuapp.com"
- Partenaire: "https://archicloud-j-partner-ultima.herokuapp.com"






