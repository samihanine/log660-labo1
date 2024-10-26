## Login
- #### description: Login to the system
- #### url: http://localhost:8082/login
- #### body:  
        {
            "username": 'NatalieLHill18@hotmail.com', 
            "password": 'Yae4yeipha'
        }
- #### method: POST
- #### response: 
        {
            "token": "{token value}",
            "type": "Bearer",
        }

# **Note:** The token value should be used in the Authorization header for all requests.

## Get Films by Filters
- #### description: Get films filtered by various parameters
- #### url: http://localhost:8082/films?startYear=1997&endYear=2004&language=English&country=USA&title=Black%20in%20men&genre=Comedy&director=A%20F&actor=A%20C&page=1&pageSize=7
- #### method: GET
- #### parameters:
      - `startYear` (optional): The start year for filtering films.
      - `endYear` (optional): The end year for filtering films.
      - `language` (optional): The language of the films.
      - `country` (optional): The country of the films.
      - `title` (optional): The title of the films.
      - `genre` (optional): The genre of the films.
      - `director` (optional): The director of the films.
      - `actor` (optional): The actor in the films.
      - `page` (optional): The page number for pagination.
      - `pageSize` (optional): The number of films per page.
- #### response:
        {
            "films": [{
                    "title": "Men in Black II",
                    "year": 2002,
                    "id": 120912
                },{
                    "title": "Men in Black",
                    "year": 1997,
                    "id": 119654
                }
            ],
            "total": 4,
            "totalPages": 1,
            "pageSize": 7,
            "page": 1
        }


## Get Film Details
- #### description: Get details of a film by ID
- #### url: http://localhost:8082/films/{id}
- #### method: GET
- #### response:
        {
            "id": 120912,
            "title": "Men in Black II",
            "year": 2002,
            "countries": [
                "USA"
            ],
            "language": "English",
            "duration": 88,
            "genres": [
                "Action",
                "Sci-Fi",
                "Comedy"
            ],
            "director": {
                "id": 1756,
                "name": "Barry Sonnenfeld"
            },
            "writers": [
                "Robert Gordon",
                "Lowell Cunningham"
            ],
            "roles": [
                {
                    "id": 206257,
                    "name": "Rosario Dawson",
                    "pseudo": "Laura Vasquez"
                },
                {
                    "id": 189144,
                    "name": "David Cross",
                    "pseudo": "Newton"
                },
                {
                    "id": 809344,
                    "name": "Michael Bailey Smith",
                    "pseudo": "Creepy"
                },
                {
                    "id": 1800,
                    "name": "Rip Torn",
                    "pseudo": "Zed"
                },
                {
                    "id": 1724,
                    "name": "Tony Shalhoub",
                    "pseudo": "Jack Jeebs"
                },
                {
                    "id": 169,
                    "name": "Tommy Lee Jones",
                    "pseudo": "Kevin Brown, Agent Kay"
                },
                {
                    "id": 1223,
                    "name": "Lara Flynn Boyle",
                    "pseudo": "Serleena"
                },
                {
                    "id": 424216,
                    "name": "Johnny Knoxville",
                    "pseudo": "Scrad"
                },
                {
                    "id": 414789,
                    "name": "Colombe Jacobsen-Derstine",
                    "pseudo": "Hailey"
                },
                {
                    "id": 226,
                    "name": "Will Smith",
                    "pseudo": "Agent Jay"
                }
            ],
            "poster": "http://ia.media-imdb.com/images/M/MV5BMTQwMjA5MDk2M15BMl5BanBnXkFtZTYwOTQ5Nzg5._V1._SX89_SY140_.jpg",
            "resume": "Agent J needs help so he is sent to find Agent K and restore his memory.",
            "trailers": [
                "http://a772.g.akamai.net/5/772/51/3a5ef4460fab04/1a1a1aaa2198c627970773d80669d84574a8d80d3cb12453c02589f25382f668c9329e0375e8177ae955ca3799026392ff64d2319a0867c93596f964c3f5/men_in_black_m320.mov",
                "http://a772.g.akamai.net/5/772/51/954c5b867b2f1d/1a1a1aaa2198c627970773d80669d84574a8d80d3cb12453c02589f25382f668c9329e0375e8177ae955ca3799026392ff64d2319a0867c93596f964c3f5/men_in_black_m480.mov",
                "http://a772.g.akamai.net/5/772/51/954c5b867b2f1d/1a1a1aaa2198c627970773d80669d84574a8d80d3cb12453c02589f25382f668c9329e0375e8177ae955ca3799026392ff64d2319a0867c93596f964c3f5/mib_tlr1_m240.mov",
                "http://a772.g.akamai.net/5/772/51/954c5b867b2f1d/1a1a1aaa2198c627970773d80669d84574a8d80d3cb12453c02589f25382f668c9329e0375e8177ae955ca3799026392ff64d2319a0867c93596f964c3f5/mib_tlr1_m480.mov",
                "http://a772.g.akamai.net/5/772/51/23506940c3ebac/1a1a1aaa2198c627970773d80669d84574a8d80d3cb12453c02589f25382f668c9329e0375e8177ae955ca3799026392ff64d2319a0867c93596f964c3f5/men_in_black_m240.mov",
                "http://a772.g.akamai.net/5/772/51/954c5b867b2f1d/1a1a1aaa2198c627970773d80669d84574a8d80d3cb12453c02589f25382f668c9329e0375e8177ae955ca3799026392ff64d2319a0867c93596f964c3f5/mib_tlr1_m320.mov"
            ]
        }


## Get All Countries
- #### description: Get all countries
- #### url: http://localhost:8082/data/countries
- #### method: GET
- #### response:
        [
            {
                "id": 1,
                "name": "Country Name"
            }
        ]

## Get All Genres
- #### description: Get all genres
- #### url: http://localhost:8082/data/genres
- #### method: GET
- #### response:
        [
            {
                "id": 1,
                "name": "Genre Name"
            }
        ]


## Get All Languages
- #### description: Get all languages
- #### url: http://localhost:8082/data/languages
- #### method: GET
- #### response:
      [
          "Language Name 1",
       "Language Name 2",
          "Language Name 3"
      ]

## Get Contributors (Actors, Directors)
- #### description: Get contributor details
- #### url: http://localhost:8082/contributors/{id}
- #### method: GET
- #### response:
        {
            "id": 1,
            "name": "Contributor Name",
            "role": "Contributor Role",
            "biography": "Contributor Biography",
            "birthDate": "Contributor Birth Date",
            "birthPlace": "Contributor Birth Place",
            "photo": "Contributor Image"
        }

## Current user
- #### description: Get information about current user
- #### url: http://localhost:8082/me
- #### method: GET
  - #### response:
          {
              "email": "NatalieLHill18@hotmail.com",
              "phone": "613-246-6128",
              "address": "2439 Parkdale Ave",
              "city": "Brockville",
              "state": "ON",
              "zip": "K6V 5T3",
              "name": "Hill",
              "firstName": "Natalie",
              "birthDate": "1946-01-10"
          }

## Rent Film
- #### description: Rent a film
- #### url: http://localhost:8082/rentals/{id}
- #### method: POST
- #### response:
        - 200 {"message", "Film rented successfully"}
        - 409 {"code": "CHECK_MAX_RESERVATIONS", message", "You have reached the maximum number of rentals"}
        - 409 {"code": "NO_AVAILABLE_COPY", "message": "Film not available for rent"}
