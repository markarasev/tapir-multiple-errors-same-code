Steps to reproduce:
 - `sbt 'runMain Test'`
 - open http://localhost:8080/docs
 - navigate to GET /test responses 
 - check 400's response description and schema

There's only a single bad request response with the description of error 1 and
the schema of error 2:

```yaml
'400':
  description: Bad request - Error 1.
  content:
    application/json:
      schema:
        $ref: '#components/schemas/Error2'
```
