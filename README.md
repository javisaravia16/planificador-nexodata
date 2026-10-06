# Simulador de planificación · NexoData

> PSP Tema 2. Procesos. 2.º A DAM · **Javier Saravia Ogazon**


## 1. ¿?¿?=)(/(/%&$·*^^¨Ñ_^ÑOJKJGE·!···ª%(/&()/=???''0998872661987562ñ`+´ñ`ñ+`ñññl##@#~@#€¬#@~##¬@#~¬@¬¬¬#¬##¬Que hace?

El programa lee un archivo con una lista de trabajos (el nombre, cuándo llegan y cuánto tiempo necesitan) y simula en qué orden los atendería un ordenador con tres métodos distintos: FCFS (por orden de llegada), SJF (primero el más corto) y Round Robin (por turnos). Para cada método enseña un diagrama de quién usa el procesador en cada minuto, una tabla con cuánto espera y cuánto tarda cada trabajo, y cuántas veces se cambia de trabajo. Con la opción --traza también cuenta paso a paso cómo va cambiando el estado de cada trabajo.

## 2. Cómo compilar y ejecutar

**Desde la terminal**, en la carpeta del proyecto:

```bash
javac -encoding UTF-8 -d out src/planificador/*.java
java -cp out planificador.Main datos/ejemplo_clase.csv rr 2 --traza
```

**Desde IntelliJ IDEA:** 
Abro la carpeta del proyecto con File → Open.
Compruebo que el JDK elegido es JDK 21 en File → Project Structure → Project
Marco la carpeta src como carpeta de código.
Creo la configuración de ejecución en Run → Edit Configurations → + → Application.
En Main class escribo planificador.Main.
En Program arguments escribo datos/ejemplo_clase.csv rr 2 --traza.
En Working directory pongo la carpeta del proyecto. Es importante: si no, el programa no encuentra el archivo de datos y avisa de que no lo encuentra.
Pulso el botón verde de ejecutar.

<img width="1289" height="290" alt="image" src="https://github.com/user-attachments/assets/6d2fa991-1738-4202-bf5d-087630f8e31a" />


## 3. Diseño
Main, recibe lo que escribe el usuario y lo comprueba
LectorProcesos: lee el archivo de datos.
Proceso: Guarda los datos de un trabajo y lo que va pasando con el, con eso calcula su espera y tiempo total
EstadoProceso: la lista de estados posibles
Planificador: el bucle de simulacion, que es igual para los tres metodos
FCFSM, SFJ y RodundRobin: deciden a quien le toca entrar y cuando hay que quitarle el procesador a quien lo esta usando
Resultado y transicion: guardan lo que sale de la simulacion, como es el diagrama, los tiempos y los cambios de estado,
Informe: lo enseña por pantalla

## 4. Verificación (tarea 4)

### 4.1 verificacion.csv resuelto a mano
<img width="842" height="370" alt="image" src="https://github.com/user-attachments/assets/74c6b969-f0d2-46b1-9a15-a2524dd6db11" />

### 4.2 Comparación con el programa
Si, la salida del programa coincide con mi resolucion a mano, tanto en FCFS como en Round Robin con q=2.

### 4.3 hueco.csv
Entre los instantes 2 y 5 no hay ningún trabajo que hacer. El proceso X termina en el instante 2, y los siguientes (Y y Z) no llegan hasta el 5. Durante esas tres unidades la CPU se queda parada, esperando. El programa no se atasca ni se salta ese tiempo, deja pasar el reloj sin que nadie use la CPU. En el diagrama se ve como un guion - en las columnas 2, 3 y 4. Esos tres huecos no se cuentan como espera de ningún proceso, porque ninguno había llegado todavía.

| nocturno.csv | Retorno medio | Espera media | Respuesta media | Cambios de contexto |
|---|---|---|---|---|
| FCFS | 14,00 | 10,00 | 10,00 | 5 |
| SJF | 11,67 | 7,67 | 7,67 | 5 |
| RR q=1 | 12,33 | 8,33 | 1,50 | 22 |
| RR q=2 | 1217 | 8,17 | 2,83 | 11 |
| RR q=4 | 14,50 | 10,50 | 6,17 | 8 |

### 5. Analisi y recomendacion de NexoData

1. SJF, con 7,67 minutos. Gana porque deja pasar primero a los procesos cortos: LOGS (1 min), INFORME y AVISOS (2 min) y RUTAS (4 min) salen antes que BACKUP (6 min)
2. 14 minutos. Llega en el instante 3 y no entra hasta el 17, a pesar de que solo necesita 1 minuto. Se llama efecto convoy. Lo provoca sobre todo FACTURA, que llega primero y ocupa la CPU 9 minutos seguidos
3. BACKUP espera 16 minutos y termina en el 24, el último
4. La respuesta media baja de 6,17 a 1,50: todos empiezan antes
5. RR q=4 tiene 10,50 de espera y FCFS 10,00, así que es un poco peor
6. En la captura, casi todos los procesos aparecen en en ejecución aunque la columna CPU marca 0 %. Windows no distingue entre un proceso que está usando la CPU y uno que está parado esperando un evento. En mi simulador esos estados son distintos: Ejecución es el que tiene la CPU en ese instante, Listo el que espera su turno y Bloqueado el que espera algo que no es la CPU, como el disco o red. Mi simulador nunca usa Bloqueado porque los procesos del CSV solo tienen ráfagas de CPU. Los procesos que aparecen como suspendido serían lo más parecido a Bloqueado.
 <img width="1028" height="585" alt="image" src="https://github.com/user-attachments/assets/7da5af74-8faf-4e18-adb6-22fda59715fb" />


### Recomendación
Para los trabajos nocturnos usaría SJF: de noche nadie espera una respuesta inmediata, y SJF es el que menos espera de media, 
7,67 frente a 10,00 de FCFS, con solo 5 cambios de contexto.
Round Robin reparte mejor, pero hace más cambios (22 con q=1) y termina los trabajos más tarde
