exceptional
===========


* [API Design](API Design.md)
* [Presentation Tier](Presentation Design.md)

## Content Provider

In this project, the client is a presentation layer for a backend server. The purpose of the client is to fetch alpha state card data from
the server, collect which checklist items are done from the user, and send those checklist items to the server. While we expect the phone to
routinely have internet access, using a ContentProvider will allow us to have a local cache when the network is not provided.

On the client, there are several entities.
1) The alphas state cards and checklists will be fetched using json. We plan to store the json entity in a local cache.

### Table: alpha_state_cards
| Column | Type  |
| --- | --- | 
| version | text | 
| json | blob |


2) List of teams for the user. For most users this will be empty or one team; a power user might manage multiple teams. 

### Table: teams
| Column | Type  |
| --- | --- | 
| id | integer | 
| name | string |

3) List of checked items for a given team. This will be fetched using json. We plan to store the json entity in a local cache.

### Table: checked_items
| Column | Type  |
| --- | --- | 
| team_id | integer |
| json | blob |

4) List of comments for a given team. This will be fetched using json. We plan to store the json entity in a local cache.

### Table: comments
| Column | Type  |
| --- | --- | 
| team_id | integer |
| json | blob |

5) The current application state. This includes the current user google email address, the selected team id and team name, and a authentication token for using the json integration layer. At present, we are exploring whether to store this information in a bundle or in a content provider 


On the server, the database is defined by this schema:
https://github.com/professor/semat/blob/master/db/schema.rb

## Application Tier
* /entities/alpha
* /entities/card (belongs to an alpha)
* /entities/checklist_item (belongs to a state card)
* /entities/team 
* /entities/user (CRUD)
* /entities/project_state (CRUD)
* /entities/application_state (CRUD)


## Integration Tier


