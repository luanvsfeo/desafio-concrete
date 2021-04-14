# Desafio - Capitulo Java
REDO do desafio da concrete com novos aprendizados do academy

## Funções disponiveis na API
 - Criação de usuario
 - Login
 - Visualização de perfil do usuario
 
## Tecnologias utilizadas 
 - Spring boot
 - JPA/Hibernate
 - H2
 - MySQL


## Endpoints

#### Criação ( /user/create )
```
    {
        "name": "João da Silva",
        "email": "joao@silva.org",
        "password": "hunter2",
        "phones": [
            {
                "number": "987654321",
                "ddd": "21"
            }
        ]
    }
```

#### Login ( /user/login )
```
    {
        "email": "joao@silva.org",
        "password": "hunter2"
    }
```
