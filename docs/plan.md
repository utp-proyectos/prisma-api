## Fase 1 — Configuración base del proyecto

### 1. application.properties

Todo lo que necesitas configurar antes de escribir una línea de código de negocio. Sin esto nada arranca correctamente.

- Nombre de la app y puerto
- Conexión a PostgreSQL (host, puerto, nombre de BD, usuario, contraseña)
- Configuración de JPA (dialecto, DDL auto en update para desarrollo, mostrar SQL)
- Conexión a Redis (host, puerto, base de datos 0)
- Clave secreta para JWT y su tiempo de expiración
- Credenciales OAuth2 de Google y GitHub
- Configuración de correo SMTP

### 2. Estructura de paquetes

Define la estructura antes de crear clases para no reorganizar después:

```
prisma-api
├── config/
├── security/
│   ├── jwt/
│   └── oauth2/
├── domain/
│   ├── user/
│   ├── team/
│   ├── project/
│   ├── channel/
│   ├── message/
│   ├── task/
│   ├── event/
│   └── canvas/
├── common/
│   ├── exception/
│   └── response/
└── infrastructure/
    ├── redis/
    └── mail/
```

## Fase 2 — Modelos y base de datos
Construyes todos los modelos antes de tocar seguridad ni servicios porque todo lo demás depende de ellos.
### 3. Enumeraciones
Créalas primero porque los modelos las referencian:

- Role — roles globales: ADMIN, USER
- TeamRole — roles dentro de un equipo: OWNER, ADMIN, MEMBER, VIEWER
- AuthProvider — LOCAL, GOOGLE, GITHUB
- TaskStatus — TODO, IN_PROGRESS, DONE
- TaskPriority — LOW, MEDIUM, HIGH

### 4. Entidad User
Es la entidad central de la que todo depende:

- id (UUID)
- name
- email (único)
- password (nullable — si usa OAuth no tiene contraseña)
- avatar
- provider (AuthProvider)
- emailVerified (boolean)
- role (Role — rol global en la plataforma)
- createdAt, updatedAt

### 5. Entidad Team

- id, name, description, avatar
- createdAt, updatedAt

### 6. Entidad TeamMember
Tabla intermedia entre User y Team con el rol:

- id
- relación @ManyToOne a User
- relación @ManyToOne a Team
- role (TeamRole)
- joinedAt
- 
### 7. Entidad Project

- id, name, description
- relación @ManyToOne a Team
- createdAt, updatedAt

### 8. Entidad Channel

- id, name (general, off-topic, etc.)
- relación @ManyToOne a Project
- isPrivate (boolean)
- createdAt

### 9. Entidad Message

- id, content
- relación @ManyToOne a User (sender)
- relación @ManyToOne a Channel
- createdAt, editedAt
- deleted (boolean — soft delete)

### 10. Entidad Task

- id, title, description
- status (TaskStatus), priority (TaskPriority)
- relación @ManyToOne a Project
- relación @ManyToOne a User (assignee, nullable)
- dueDate, createdAt, updatedAt

### 11. Entidad CalendarEvent

- id, title, description
- startDate, endDate
- allDay (boolean)
- relación @ManyToOne a Project
- relación @ManyToOne a User (creador)
- createdAt

### 12. Entidad CanvasState

- id
- relación @OneToOne a Project
- data (TEXT — JSON serializado de Fabric.js)
- updatedAt

### 13. Entidad Invitation

- id, email
- relación @ManyToOne a Team
- relación @ManyToOne a User (quien invitó)
- token (UUID único para el link del correo)
- expiresAt
- accepted (boolean)

### 14. Entidad EmailVerification

- id
- relación @OneToOne a User
- token
- expiresAt


## Fase 3 — Repositorios
Un repositorio por entidad. La mayoría extienden JpaRepository. Aquí defines solo las queries custom que necesitarás, con nombres descriptivos:

### 15. UserRepository

- findByEmail(String email)
- existsByEmail(String email)

### 16. TeamMemberRepository

- findByUserId(String userId) — todos los equipos de un usuario
- findByTeamId(String teamId) — todos los miembros de un equipo
- existsByUserIdAndTeamId(String userId, String teamId) — verificar membresía
- findByUserIdAndTeamId(String userId, String teamId) — obtener el rol

### 17. ProjectRepository

- findByTeamId(String teamId)

### 18. ChannelRepository

- findByProjectId(String projectId)

### 19. MessageRepository

- findByChannelIdOrderByCreatedAtDesc(String channelId, Pageable pageable) — mensajes paginados

### 20. TaskRepository

- findByProjectId(String projectId)
- findByProjectIdAndStatus(String projectId, TaskStatus status)
- findByAssigneeId(String userId)

### 21. CalendarEventRepository

- findByProjectId(String projectId)
- findByProjectIdAndStartDateBetween(String projectId, LocalDateTime from, LocalDateTime to)

### 22. CanvasStateRepository

- findByProjectId(String projectId)

### 23. InvitationRepository

- findByToken(String token)
- findByEmailAndTeamId(String email, String teamId)

### 24. EmailVerificationRepository

- findByToken(String token)
- findByUserId(String userId)


## Fase 4 — DTOs y respuestas
Nunca expones las entidades JPA directamente en los endpoints. Los DTOs son las clases que viajan entre el frontend y el backend.

### 25. Clase ApiResponse<T>
Wrapper genérico para todas las respuestas:

```
{
  "success": true,
  "data": { ... },
  "message": "ok",
  "timestamp": "..."
}
```

### 26. DTOs de request — lo que Angular envía al backend:

- RegisterRequest — email, password, name
- LoginRequest — email, password
- CreateTeamRequest, UpdateTeamRequest
- CreateProjectRequest, UpdateProjectRequest
- CreateChannelRequest
- CreateTaskRequest, UpdateTaskRequest, MoveTaskRequest
- CreateEventRequest, UpdateEventRequest
- InviteMemberRequest — email, teamId, role
- UpdateCanvasRequest — data (JSON)

### 27. DTOs de response — lo que el backend devuelve a Angular:

- AuthResponse — token JWT, datos básicos del usuario
- UserResponse — id, name, email, avatar (nunca el password)
- TeamResponse, ProjectResponse, ChannelResponse
- MessageResponse
- TaskResponse
- CalendarEventResponse
- TeamMemberResponse — usuario + su rol en el equipo

### 28. Clase ChatMessage (para WebSocket)

- sender, content, channelId, projectId, teamId, timestamp, action

### 29. Clase KanbanEvent, CalendarWsEvent, CanvasEvent (para WebSocket)
Similares al ChatMessage pero para cada módulo en tiempo real.

## Fase 5 — Seguridad
Ahora que tienes los modelos y repositorios, implementas la seguridad que los usa.

### 30. JwtService

- generateToken(User user) — genera el JWT con userId, email, role
- extractUserId(String token)
- extractEmail(String token)
- isValid(String token)
- Usa la clave secreta del application.properties

### 31. JwtAuthenticationFilter
Filtro que se ejecuta en cada petición HTTP:

- Extrae el token del header Authorization: Bearer xxx
- Lo valida con JwtService
- Si es válido, carga el usuario y lo pone en el SecurityContext
- Si es inválido, deja pasar sin autenticar (el endpoint decide si requiere auth)

### 32. CustomUserDetailsService
Implementa UserDetailsService de Spring Security:

- loadUserByUsername(String email) — busca el usuario en PostgreSQL por email
- Lo necesita Spring para el login por correo y contraseña

### 33. CustomOAuth2UserService
Implementa DefaultOAuth2UserService:

- Recibe los datos de Google/GitHub
- Busca el usuario por email en PostgreSQL
- Si no existe lo crea con provider = GOOGLE o GITHUB
- Si existe pero con otro provider, lanza error explicativo

### 34. OAuth2SuccessHandler

- Se ejecuta cuando OAuth2 completa exitosamente
- Genera el JWT con JwtService
- Redirige a http://localhost:4200/auth/callback?token=xxx

### 35. WebSocketChannelInterceptor
Intercepta cada frame STOMP antes de procesarlo:

- En CONNECT: extrae el JWT del header, lo valida, setea el usuario en la sesión
- En SUBSCRIBE: verifica que el usuario tiene acceso al topic al que intenta suscribirse (pertenece al equipo)
- En SEND: verifica que puede enviar mensajes al destino

### 36. SecurityConfig
Bean principal de Spring Security:

- Define qué rutas son públicas (/auth/**, /ws/**, /oauth2/**)
- Registra el JwtAuthenticationFilter antes del filtro de username/password
- Configura OAuth2 login con el CustomOAuth2UserService y el OAuth2SuccessHandler
- Configura CORS (permite peticiones desde localhost:4200)
- Deshabilita CSRF (no necesario con JWT)
- Sesión stateless (no usa sesiones HTTP, solo JWT)

### 37. CorsConfig
Bean separado para CORS más detallado:

- Permite origen http://localhost:4200
- Permite métodos GET, POST, PUT, DELETE, PATCH
- Permite headers Authorization, Content-Type
- Permite credenciales

## Fase 6 — Configuración de infraestructura

### 38. RedisConfig

- Bean RedisTemplate con serialización JSON (para guardar objetos, no solo strings)
- Bean RedisMessageListenerContainer — registra los suscriptores con sus topics
- Bean MessageConverter para que Spring WebSocket serialice a JSON automáticamente

### 39. WebSocketConfig

- Registra el endpoint /ws con SockJS fallback
- Configura el broker con prefijo /topic
- Configura el prefijo /app para los @MessageMapping
- Registra el WebSocketChannelInterceptor
- Permite origen http://localhost:4200

### 40. MailConfig

- Bean JavaMailSender configurado con los datos SMTP del application.properties
- Bean para templates de correo (si usas Thymeleaf para los emails)

### 41. JacksonConfig

- Configura el ObjectMapper global
- Serialización de fechas como ISO 8601
- Ignora campos nulos en las respuestas
- Manejo de tipos polimórficos para Redis


## Fase 7 — Manejo de errores

### 42. Excepciones custom
Una por cada tipo de error de negocio:

- ResourceNotFoundException — cuando un recurso no existe (404)
- UnauthorizedException — cuando no tiene permisos (403)
- EmailAlreadyExistsException — registro duplicado (409)
- InvalidTokenException — token expirado o inválido (401)
- TeamMembershipException — no pertenece al equipo (403)
- InvitationExpiredException — invitación vencida (410)

### 43. GlobalExceptionHandler
Clase con @RestControllerAdvice que captura todas las excepciones y devuelve respuestas consistentes con ApiResponse:

- Maneja cada excepción custom
- Maneja MethodArgumentNotValidException — errores de validación de campos
- Maneja Exception genérica — errores inesperados (500)


## Fase 8 — Servicios
Ahora sí la lógica de negocio. Cada servicio usa los repositorios, lanza las excepciones custom y devuelve DTOs.
### 44. AuthService

- register(RegisterRequest) — crea usuario, hashea password con BCrypt, envía email de verificación
- login(LoginRequest) — valida credenciales, genera JWT
- verifyEmail(String token) — marca el email como verificado
- resendVerification(String email)

### 45. UserService

- findById(String id)
- updateProfile(String userId, UpdateProfileRequest)
- updateAvatar(String userId, MultipartFile file)

### 46. TeamService

- createTeam(String userId, CreateTeamRequest)
- getTeamsByUser(String userId)
- updateTeam(String teamId, UpdateTeamRequest)
- deleteTeam(String teamId)
- getMembers(String teamId)
- updateMemberRole(String teamId, String userId, TeamRole role)
- removeMember(String teamId, String userId)

### 47. InvitationService

- inviteMember(String teamId, InviteMemberRequest) — crea invitación, envía email
- acceptInvitation(String token) — valida token, agrega miembro al equipo
- revokeInvitation(String invitationId)

### 48. ProjectService

- createProject(String teamId, CreateProjectRequest)
- getProjectsByTeam(String teamId)
- updateProject(String projectId, UpdateProjectRequest)
- deleteProject(String projectId)

### 49. ChannelService

- createChannel(String projectId, CreateChannelRequest)
- getChannelsByProject(String projectId)
- deleteChannel(String channelId)

### 50. ChatService

- processMessage(ChatMessage message) — persiste en PostgreSQL y publica en Redis
- getMessageHistory(String channelId, Pageable pageable) — mensajes paginados
- deleteMessage(String messageId, String userId)

### 51. TaskService

- createTask(String projectId, CreateTaskRequest)
- getTasksByProject(String projectId)
- updateTask(String taskId, UpdateTaskRequest)
- moveTask(String taskId, MoveTaskRequest) — cambia columna, publica en Redis
- deleteTask(String taskId)
- assignTask(String taskId, String userId)

### 52. CalendarService

- createEvent(String projectId, CreateEventRequest)
- getEventsByProject(String projectId)
- updateEvent(String eventId, UpdateEventRequest)
- deleteEvent(String eventId)

### 53. CanvasService

- getCanvasState(String projectId)
- updateCanvasState(String projectId, UpdateCanvasRequest) — persiste y publica en Redis

### 54. NotificationService

- notifyUser(String userId, String message, String type) — publica en /topic/user/{userId}/notifications

### 55. RedisPublisher

- publish(String topic, Object message) — método genérico que usan todos los servicios

### 56. RedisSubscriber

- Implementa MessageListener
- Recibe mensajes de Redis y los reenvía por WebSocket con SimpMessagingTemplate
- Necesitas uno por módulo o uno genérico que detecte el topic y enrute

### 57. MailService

- endVerificationEmail(String email, String token)
- endInvitationEmail(String email, String teamName, String token)
- endPasswordResetEmail(String email, String token) — si decides agregar recuperación de contraseña


## Fase 9 — Controladores REST
Uno por módulo. Todos usan @PreAuthorize para verificar permisos y devuelven ApiResponse.
### 58. AuthController — rutas públicas

- OST /api/auth/register
- OST /api/auth/login
- ET  /api/auth/verify-email?token=xxx
- OST /api/auth/resend-verification

### 59. UserController — rutas autenticadas

- GET  /api/users/me
- PUT  /api/users/me
- POST /api/users/me/avatar

### 60. TeamController

- POST   /api/teams
- GET    /api/teams — equipos del usuario autenticado
- GET    /api/teams/{teamId}
- PUT    /api/teams/{teamId}
- DELETE /api/teams/{teamId}
- GET    /api/teams/{teamId}/members
- PUT    /api/teams/{teamId}/members/{userId}/role
- DELETE /api/teams/{teamId}/members/{userId}

### 61. InvitationController

- POST /api/teams/{teamId}/invite
- GET  /api/invitations/accept?token=xxx
- DELETE /api/invitations/{invitationId}

### 62. ProjectController

- POST   /api/teams/{teamId}/projects
- GET    /api/teams/{teamId}/projects
- GET    /api/projects/{projectId}
- PUT    /api/projects/{projectId}
- DELETE /api/projects/{projectId}

### 63. ChannelController

- POST   /api/projects/{projectId}/channels
- GET    /api/projects/{projectId}/channels
- DELETE /api/channels/{channelId}

### 64. MessageController — solo para historial, el envío va por WebSocket

- GET /api/channels/{channelId}/messages — paginado

### 65. TaskController

- POST   /api/projects/{projectId}/tasks
- GET    /api/projects/{projectId}/tasks
- PUT    /api/tasks/{taskId}
- DELETE /api/tasks/{taskId}
- PATCH  /api/tasks/{taskId}/assign

### 66. CalendarController

- POST   /api/projects/{projectId}/events
- GET    /api/projects/{projectId}/events
- PUT    /api/events/{eventId}
- DELETE /api/events/{eventId}

### 67. CanvasController

- GET /api/projects/{projectId}/canvas


## Fase 10 — Controladores WebSocket

### 68. ChatWsController

- @MessageMapping("/chat.send")
- @MessageMapping("/chat.delete")

### 69. KanbanWsController

- @MessageMapping("/kanban.create")
- @MessageMapping("/kanban.move")
- @MessageMapping("/kanban.update")
- @MessageMapping("/kanban.delete")

### 70. CalendarWsController

- @MessageMapping("/calendar.create")
- @MessageMapping("/calendar.update")
- @MessageMapping("/calendar.delete")

### 71. CanvasWsController

- @MessageMapping("/canvas.update") — sincroniza cambios en la pizarra en tiempo real


### Orden de implementación recomendado

- application.properties completo
- Todos los modelos y enumeraciones
- Todos los repositorios
- DTOs y ApiResponse
- Excepciones custom y GlobalExceptionHandler
- JwtService
- CustomUserDetailsService
- SecurityConfig básico (solo login por correo por ahora)
- AuthService + AuthController — ya puedes registrar y hacer login
- JwtAuthenticationFilter
- UserService + UserController
- TeamService + TeamController
- ProjectService + ChannelService + sus controladores
- RedisConfig + WebSocketConfig
- WebSocketChannelInterceptor
- ChatService + ChatWsController + RedisPublisher + RedisSubscriber
- TaskService + TaskController + KanbanWsController
- CalendarService + CalendarController + CalendarWsController
- CanvasService + CanvasController + CanvasWsController
- MailConfig + MailService + InvitationService
- CustomOAuth2UserService + OAuth2SuccessHandler — agrega Google y GitHub
- NotificationService