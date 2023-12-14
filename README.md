# Curso Java Expert da plataforma DevSuperior #

Inicialmente foi criada uma API Rest denominada DS-catalog. Foi implementado um CRUD completo com camadas de desenvolvimento (Entities, Controllers, Repositories, Services e DTO). 

Posteriormente foram realizados os testes automatizados utilizando JUnit e Mockito.
- Os testes foram feitos com Fixtures para evitar repetições e melhor orgnanização (@BeforeAll, @AfterAll, @BeforeEach, @AfterEach).
- Foi criada uma classe Factory para a criação de entidades e DTOs utilizados nos testes, conferindo maior organização e melhor legebilidade.
- Banco de dados utilizado: H2.
- Camadas testadas: repositories, services
