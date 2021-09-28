# FINT Xledger Adapter

## Configuration
### Mandatory properties
| Key | Description | Default value |
| ---- | ---- | ---- |
| fint.adapter.organizations | Name of organisation in FINT | |
| fint.xledger.graphql.token | Access token for accessing Xledger | |
| fint.xledger.mva | Map of merverdiavgift metatypes and values | Ex: HIGH_LEVEL=250, MID_LEVEL=150, LOW_LEVEL=120, SPECIAL_RATE=111 |
| fint.xledger.ownerDbId | DbId for the owner-oranization in Xledger. Topmost elment in the companies structure. | |
| fint.xeldger.invoiceLayoutDbId | Fakturamal is not exposed through GraphQL. This is read from a manualy created SalesOrder. | |
| fint.xeldger.currencyDbId | ObjectValues of type CURRENCY | |
| fint.xeldger. digistToCompareSalgsordregruppeAndProduct | How many digit to compare between Salgsordregruppe.code and Product.code | |
| fint.client.details.username | FINT Client oauth |  |
| fint.client.details.password | FINT Client oauth |  |
| fint.client.details.clientId | FINT Client oauth |  |
| fint.client.details.accessTokenUri | FINT Client oauth |  |
| fint.client.details.scope | FINT Client oauth |  |
| fint.client.details.clientSecret | FINT Client oauth |  |
| fint.client.details.assetId | FINT Client oauth |  |
| fint.client.details.enabled | FINT Client oauth |  |
| fint.oauth.username | FINT Adapter oauth |  |
| fint.oauth.password | FINT Adapter oauth |  |
| fint.oauth.access-token-uri | FINT Adapter oauth |  |
| fint.oauth.client-id | FINT Adapter oauth |  |
| fint.oauth.client-secret | FINT Adapter oauth |  |
| fint.oauth.scope | FINT Adapter oauth |  |
| fint.oauth.enabled | FINT Adapter oauth |  |

### Optional properties
| Key | Description | Default value |
| ---- | ---- | ---- |
| fint.xledger.graphql.pageSize | ---- | ---- |

