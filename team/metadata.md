# Structure de métadonnées

## Introduction : définition
Un fichier de métadonnées est un fichier reprenant l’ensemble des modifications apportées à un fichier au cours du temps. En d’autres termes, il contient l’entièreté des commits depuis la création du diagramme, un commit pouvant être un simple commit mais également un branch, un merge ou un revert.

## Architecture
Dans cette section, nous allons discuter de l’architecture de stockage des fichiers de métadonnées en eux-même.

### Stockage
Nous avons fait le choix de stocker un fichier par branche et par diagramme. Un fichier de métadonnées est donc créé lors de la création d’un diagramme, d'une nouvelle branche ou lors de l’import d'un fichier `.tex`. Ces fichiers doivent être stockés dans le même répertoire que le projet et leur nom doivent commencer par un `.`, de sorte à être cachés. Si l’utilisateur désire changer de répertoire le projet avec les fichiers de métadonnées correspondants, il devra impérativement passer par l’application (ou déplacer manuellement tous les fichiers cachés). 
Dans le cas où le fichier `.tex` est déplacé seul, le projet devra être ré-importé et sera considéré comme un nouveau projet. Autrement dit, le fichier est légitimé en tant que projet par l’existence d’un ou plusieurs fichiers de métadonnées associés dans le même répertoire.

### Nom de fichier
Le nom d’un fichier de métadonnées doit commencer par un `.project`, suivi de l’identifiant du projet (unique, généré par la base de données), suivi de _branch`, suivi de l’identifiant de la branche (généré également par la base de données). Ce nom ne comporte pas d’extension. 

Exemple: Soit un projet dont l’identifiant est `1`, et une de ses branches dont l’identifiant est `4, le fichier de métadonnées correspondant portera le nom
```
.project1_branch4
```

## Branches et commits

### Stockage des données relatives aux branches et commits
Les données relatives aux branches et commits sont stockées dans deux nouvelles tables de la base de données.

La base de données stocke cinq informations primordiales par rapport aux branches :
1. L’identifiant du projet
2. L’identifiant de la branche
3. Le nom de la branche
4. La date de la création de la branche
5. Le statut (ouvert, supprimé, mergé) de la branche.

La branche est identifiée par le couple (1,2). Ces informations permettent ainsi de reconstituer le nom du fichier de métadonnées correspondant, contenant tous les commits associés.

Une deuxième table stocke les informations concernant les commits :
1. L’identifiant du projet (unique)
2. L’identifiant de la branche
3. L’identifiant du commit
4. Le message du commit
5. La date du commit

Un commit est identifié par le tuple (1, 2, 3). 

### Message stocké
Le message enregistré avec un commit dépend de la nature de celui-ci. Dans le cas d’un simple commit, le message est renseigné par l’utilisateur (bien que le programme puisse en suggérer un par défaut). Dans le cas d’un branch, d’un merge, ou d’un revert, le message doit être spécifié automatiquement selon les formes suivantes.

#### Branch
Dans le cas d’un branch, le message doit être le suivant:
```bash
branched $(new_branch_name) from $(origin_branch_name);
```

#### Merge
Dans le cas d’un merge, le message doit être le suivant:
```bash
merged $(origin_branch) to $(destination_branch);
```

#### Revert
Dans le cas d’un merge, le message doit être le suivant:
```bash
reverted $(branch_name) to $(commit_number);
```

#### Commit
Dans le cas d’un commit, le message est entré par l’utilisateur, mais ne pourra pas excéder 128 caractères, ni comporter de caractères spéciaux.

## Reconstitution d’un fichier de projet
Les informations présentées précédemment ont pour but de pouvoir reconstituer le contenu d’un fichier en suivant l’historique des modifications spécifiques pour obtenir une version différente de celle stockée dans le fichier `.tex, qu'il s'agisse d'une ancienne version ou d'une version parallèle. Pour ce faire, le programme doit, en partant d'un fichier vide, y appliquer les contenus des commits successifs de la branche sélectionnée jusqu’à la version désirée.

Exemple: l’utilisateur travaille sur un projet constitué de deux branches: `premiere_branche`, et `deuxieme_branche`. Il modifie pour le moment la `premiere_branche`, et veut changer vers la `deuxieme_branche`. Le programme lit alors le contenu du fichier de métadonnées de la `deuxieme_branche`, constitué de la partie commune à `premiere_branche` et `deuxieme_branche`, suivi des commits spécifiques à `deuxieme_branche`.

Comme le montre cet exemple, lors de la création d'une nouvelle branche, le fichier de métadonnées est dupliqué, ce qui permet de reconstituer entièrement le code TikZ uniquement à l'aide du fichier correspondant à cette branche.

## Contenu du fichier de métadonnées
Ce fichier doit, pour assurer la lecture par le programme, suivre une syntaxe formelle. 
Un commit suit la norme suivante:
```bash
commit $(identifiant_commit) \n 
\t +cmdtikz  \n
\t -cmdtikz  \n
\eof ou commit
```
Les \n désignent des passages à la ligne, et des \t désignent des tabulations. Un commit est clôturé soit par le commit suivant, soit par la fin de fichier.
Une addition (resp. une délétion) de commande est indiquée par un + (resp. -)  entre la tabulation et le début de la commande TikZ. La commande TikZ est la ligne en elle-même tronquée de ses deux premiers caractères.

## Remarques
Comme l’ordre des instructions TikZ n’importe pas pour le résultat graphique, il est convenu que la reconstitution d’un fichier n’est pas faite à l’identique. Cependant, cette philosophie assure la simplicité des différentes opérations (merge, revert, commit, branch).

Au-delà de l’aspect programmation, ce procédé permet d’éviter le stockage de multiples fichiers ‘.tex’, et donc garantit une simplicité pour l’utilisateur.
