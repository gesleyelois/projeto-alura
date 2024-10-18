# projeto-alura
## Link do repositorio: https://github.com/marifdam/projeto-alura

### Como rodar a aplicação:
``mvn clean install`` e ``mvn spring-boot:run``

### Como rodar os testes unitários gerar o report de cobertura:
``mvn test`` e ``mvn jacoco:report``

Voce pode ver o report em ``/target/site/jacoco/index.html``
<hr>

### Endpoints
Criar novos estudantes: POST``http://localhost:8080/user/newStudent``
```
{
	"name":"Student",
	"email":"Student@alura.com",
	"password":"0123456789"
}
```

Criar novos instrutores: POST``http://localhost:8080/user/newInstructor``
```
{
	"name":"Instructor",
	"email":"instructor@alura.com",
	"password":"0123456789"
}
```
Criar novos cursos: POST``http://localhost:8080/course/new``
```
{
	"name":"c++",
	"code":"cplusplus",
	"description":"lalalaa",
	"instructorEmail": "instructor@alura.com"
}
```
Criar novos registros: POST``http://localhost:8080/registration/new``
```
{
	"studentEmail":"student@alura.com",
	"code":"cplusplus"
}
```
Desativar cursos: POST``http://localhost:8080/course/cplusplus/inactive``

Gerar relatório: GET``http://localhost:8080/registration/report``


