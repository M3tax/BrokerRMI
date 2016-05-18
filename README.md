# BrokerRMI

## This project contains a working version of a RMI broker that works with 2 servers and n-clients.

### To test this application:

* Launch LanzadorRMI without parameters to launch the RMI Registry
* Launch Lanzador with 2 parameters (second one is optional):
  * First parameter: Type of application: -cliente (client), -serverA (self explanatory), -serverB (self explanatory) or -broker (self explanatory)
  * Second parameter: Optionally if you have the RMI Registry in a different machine you can put the IP of given machine to access it remotely.

### Execution example:
* Launch LanzadorRMI
* Launch Lanzador with option -broker
* Launch Lanzador with option -serverA or -serverB or both
* Launch Lanzador with option -client

### FAQ:
* If the client doesn't show any options in the menu, you don't have any server (A or B) launched.
