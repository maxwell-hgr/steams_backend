# Steam API Integration - RESTful API

## Sobre o Projeto
Esta API RESTful, desenvolvida em **Java Spring Boot**, permite consultar a **API da Steam** para obter dados sobre jogos, amigos e informações gerais do perfil do usuário. Além disso, utiliza o **OpenID da Steam** para autenticação.

## Tecnologias Utilizadas
- **Java 21**
- **Spring Boot**
- **Spring Security**
- **OAuth/OpenID Connect (Steam OpenID)**

## Como Funciona
1. O usuário acessa `/login` para autenticar via **OpenID da Steam**.
2. Após a autenticação, ele pode consultar endpoints protegidos para obter informações do seu perfil, jogos e amigos.

## Endpoints Disponíveis

### 1. Autenticação
#### `POST /login`
Realiza login via **Steam OpenID**, redirecionando o usuário para a autenticação da Steam.

**Resposta:**
```json
{
   "id": "76561198012345678",
  "token": "eyJhbGciOiJIUzI1NiIs..."
}
```

### 👤 2. Perfil do Usuário
#### `GET /user/profile`
Retorna informações sobre o usuário autenticado.

**Resposta:**
```json
{
  "id": "76561198012345678",
  "username": "ExampleUser",
  "avatar": "https://avatars.steamstatic.com/107bd4890790f698999dbbe33c87babdd68ab8ff_full.jpg",
   "lobbies": []
}
```

### 🎮 3. Lista de Jogos
#### `GET /user/games`
Retorna a lista de jogos do usuário autenticado.

**Resposta:**
```json
{
  "games": [
    { "appId": "393380",
       "name": "Squad",
       "banner": "https://cdn.cloudflare.steamstatic.com/steam/apps/393380/header.jpg" },
    { "appId": "252490",
       "name": "Rust", 
       "banner": "https://cdn.cloudflare.steamstatic.com/steam/apps/252490/header.jpg" }
  ]
}
```

### 👫 4. Lista de Amigos
#### `GET /user/friends`
Retorna a lista de amigos do usuário autenticado.

**Resposta:**
```json
{
  "friends": [
     {
        "id": "76561198012345678",
        "username": "ExampleUser",
        "avatar": "https://avatars.steamstatic.com/107bd4890790f698999dbbe33c87babdd68ab8ff_full.jpg",
        "lobbies": []
     },
     {
        "id": "76561198012345678",
        "username": "ExampleUser",
        "avatar": "https://avatars.steamstatic.com/107bd4890790f698999dbbe33c87babdd68ab8ff_full.jpg",
        "lobbies": []
     }
  ]
}
```

## 🔧 Configuração e Execução
1. Clone o repositório:
   ```sh
   git clone https://github.com/seu-usuario/seu-repositorio.git
   cd seu-repositorio
   ```
2. Configure as credenciais da Steam API no `application.properties`:
   ```properties
   steam.api.key=SEU_STEAM_API_KEY
   token.secret=SEU_SEGREDO_JWT
   ```
3. Compile e execute a aplicação:
   ```sh
   mvn spring-boot:run
   ```
4. Acesse `http://localhost:8080`

