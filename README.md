06/04 15h30 : 

Faire le CRUD complet de la table product 

Ajout avec un nom et un prix 

Modification du produit

Suppression par nom 
Suppression par ID 

Ajout d'un produit avec un nom de catégorie 
==> En vérifiant que la catégorie existe, sinon l'ajouter avant 

Point techniques : 

Utiliser @RequestBody, @RequestParam et @PathVariable dans les points d'API 

Ajouter @Transactional sur la classe de tests pour rollback les modifications une fois le test passé


07/04 09h30 : 

Ajouter la contrainte lorsqu'on récupère la liste de produits avec le nom, exclure le produit courant (via le productId)

Gérer les cas d'erreurs via ResponseEntity sur tous les points d'API

