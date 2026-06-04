# TRABAJO 2: ESTILOS CALL - RETURN

### Ejercicio 1 - Lectura de componentes de una URL

Crear un objeto de tipo URL en Java e imprimir los valores retornados por los métodos:
- getProtocol()
- getAuthority()
- getHost()
- getPort()
- getPath()
- getQuery()
- getFile()
- getRef()

> **URL Utilizada** : https://github.com:443/usuario/repo/issues?estado=abierto&autor=user#cristian

### Explicación de los Métodos

| Elemento | Valor obtenido | Método utilizado | Descripción |
|-----------|---------------|------------------|-------------|
| Protocolo | `https` | `getProtocol()` | Protocolo utilizado por la URL. |
| Autoridad | `github.com:443` | `getAuthority()` | Autoridad de la URL, compuesta por el host y el puerto. |
| Host | `github.com` | `getHost()` | Nombre del servidor al que apunta la URL. |
| Puerto | `443` | `getPort()` | Puerto especificado en la URL. |
| Ruta | `/usuario/repo/issues` | `getPath()` | Ruta del recurso dentro del servidor. |
| Consulta (Query) | `estado=abierto&autor=user` | `getQuery()` | Parámetros enviados en la consulta HTTP. |
| Archivo | `/usuario/repo/issues?estado=abierto&autor=user` | `getFile()` | Ruta del recurso junto con la cadena de consulta. |
| Referencia (Fragmento) | `cristian` | `getRef()` | Fragmento o referencia ubicado después del símbolo `#`. |

### Pruebas:

![alt text](ejercicio3.1/resources/cmd.png)

![alt text](ejercicio3.1/resources/salida.png)

### Conclusiones

1. La clase URL permite descomponer fácilmente una dirección web en sus diferentes componentes.
2. Los métodos de la clase facilitan la obtención de información específica como protocolo, host, puerto y parámetros de consulta.
3. Esta funcionalidad es útil para aplicaciones cliente-servidor, navegadores, crawlers y sistemas que procesan recursos web.

