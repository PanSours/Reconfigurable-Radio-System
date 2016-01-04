# Synopsis
In recent years, the development of telecommunications has created a heterogeneous and dynamic environment where coexist different radio access technologies. At the same time, users can not keep up with these technological leaps but require as consumers to receive the highest quality service compared with the contract they have.
Therefore, the decision-making capability with respect to the choice of access radio technology has been transferred to the communication devices. This process requires continuous monitoring of the operating environment. The variety of technologies but in combination with low potential energy autonomy of the devices make the monitoring process quite difficult and time consuming.
As a solution to this problem, the scientific community has proposed the use of a discrete communication channel which willbe able to transfer information on reference devices with the available radio access technologies in the territory located (Cognitive Pilot Channel).
You can read the feasibility study of this project through the following link: http://www.etsi.org/deliver/etsi_tr/102600_102699/102683/01.01.01_60/tr_102683v010101p.pdf

# Background

## Network Management Entity - Base Station

This is the software that manages a Network. In particular our software implements the following functions:

* Enabling a Base Station: A new Base Station is activated in a specific geographic area. Activation is completed by sending a data set describing the supported radio technology in the management software of Cognitive Pilot Channel.

* Disabling a Base Station: The base station is switched off.

* Operating overview of a Base Station: Essentially it is the heart of the system. But given the absence of a genuine Base Station we will simulate this process. The simulation will be based on the assumption that for each paired Mobile, a Base Station commits 5% of the available resources having the above limit 90% (that can serve simultaneously 18 Mobiles).

* Periodic updating of Cognitive Pilot Channel's information: Periodically (every t seconds) information about the current state of the network is sent to the management software of Cognitive Pilot Channel.

The radio technology is described by the following parameters:

* Network ID: Unique identifier of the network.

* Base Station ID: The ID of the Base Station that provides the specific radio technology (in the current job can consider as base ID, the IP of the machine running the application or if you are working on a machine, its port).

* Signal Strength: The transmit power.

* Network Type: The type of the network takes values ​​from the set {GSM, UMTS, WLAN, WIMACH}.

* Frequency: The transmit frequency.

* Maximum Difiorythmos (max bitrate): The maximum speed supported by the network.

* Guaranteed Difiorythmos (guatanteed bittate): The guaranteed speed supported by the network.

* Load Level: Percentage that reflects the burden of the station at a specific time.

* Provider: The provider of this technology.

* Billing: The billing model. It takes values ​​from the set {FIXED METERED, PACKET, EXPECTED, EDGE, PARIS, AUCTION}.

* Coverage Radius: The maximum distance that emits the particular Base Station.

* Geographical position: The coordinates (X, Y) of the Base Station.

* Port: The communication port of Mobiles with the particular base station.

A Base Station supports a single radio technology and belongs to a single provider. In contrast, a radio technology is supported by multiple Base Stations and also can be provided by many providers. The charging model is distinct for each provider for each technology. This means that each provider selects his own billing model.
The software management of the base station at startup reads the above information from a text file (property file). Then it stores it in memory and will wait to activate the station.
Activation can be achieved with the click of a button. After switching on, the Base Station sends to the Cognitive Pilot Channel's management software the above information. The latter practically entails activating a thread that will communicate each t seconds (parameter that will set you in the property file) with the Cognitive Pilot Channel's management software.
A terminal connects with a Base Station by sending the message ``` CONNECT <IMEI> <IMSI> <X> <Y> ```. When the Base Station receives this message, it will check if it can - resources - to serve the mobile and if the latter is located in the geographical area it serves. If either of the two conditions are not met then the cell will be rejected taking the message ``` OVERLOADED ``` or  ``` NO COVERAGE ``` appropriate. Otherwise, the user receives the message OK and the data will be stored in the memory of the Base Station.
Disabling a base station is again at the touch of a button. Disabling practically translates to deactivation of threads the Base Station and the cleaning of the memory. Alongside the connected mobile alerted disconnected. Finally, alerted the diffusion channel to disable the Base Station.

The messages received by the station are:

* ``` CONNECT <IMEI> <IMSI> <X> <Y> ```
e.g. ``` CONNECT 35395803-121326-9 310150123456789 10 10 ``` 
This command means that the user with IMSI 310150123456789 located at (10,10) wants to connect through the device IMEI 35395803-121326-9 the Base Station. Assume that all coordinates are between the points O(0,0) and A(100,100).
 
* ``` DISCONNECT <IMEI> ```

e.g. ``` DISCONNECT 35395803-121326-9 ```
The above command means that the device IMEI 35395803-121326-9 not longer Resource Base Station.

Messages posted by a Base Station are:

* ``` OK ``` in response to the ``` CONNECT/DISCONNECT ```.

* ``` OVERLOADED ``` as a response to the request ``` CONNECT ``` when it can not be served because of exceeding load level.

* ``` NO COVERAGE ``` as a response to the request ``` CONNECT ``` when it can not connect serve some device due coverage.

* ``` DISCONNECT ``` , requiring the connected mobile το βε disconnected from the Base Station.

* ``` PROFILE <BASESTATION PROFILE> ``` to inform Cognitive Pilot Channel on the current status.

The communication interface between the Mobile and Base Station we are using sockets whereas among the Base Stations and the Cognitive Pilot Channel
we are using Web Services. Furthermore, the Base Station has a rudimentary
graphical interface to present the state of (parameters, mobile and connected load) and enables Enable/disable features.



## Terminal Management Entity

The Terminal management entity is the software that is responsible for monitoring the operation of the user's device and the interaction with the user. Essentially offers a graphical user interface that allows it to carry out the following processes:

* Switching on the device: A device activation is considered as the start of the application.

* Automatic network selection: Automated network selection based on the capabilities of the device. The device itself selects the Network that is closer to the user requirements and capabilities of the device.

* Non-automatic network selection: Network selection by the user, based on a list of available and supported Networks. The list is created of a comparison of available Networks that have been sent to the Cognitive Pilot Channel and taking into consideration the device capabilities.

* Change user data: The user via appropriate GUI updates the data of which are stored in a Mobile.

* Connect to network: The device connects to the selected network. Essentially, sending the IMEI, IMSI management entity in the particular network.

* Turn off the device: As deactivation of the device is considered the end of the application. Upon termination IMEI code of the device is sent to the Base Station.

* Download updated data frame: Download the most updated data from Cognitive Pilot Channel (basically a list of radio technologies).

The capabilities of a device are being described by the following fields:

* IMEI: Unique device identifier.

* IMSI: Unique SIM card ID that carries the phone.

* Supported Network Type: The types of Networks supported by the device. Is one or more values ​​from the set {GSM, UMTS, WLAN, WIMAX}.

* Operating System: The operating system of the device, one of {Windows, Linux, Symbian, Android}.

* CPU: The operating frequency of the CPU.

* RAM: The amount of memory available in the system.

* Downloading data from the Cognitive Pilot Channel: Variable which determines if this device can receive data from the Cognitive Pilot Channel. It takes value TRUE or FALSE.

The user is being described by the following fields:

* Name: The name of the user.

* Last name: The user's last name.

* Address: The address of the user.

* Pricing Model: The model which wants to charge the user. Takes value from the set {FIXED, METERED, PACKET, EXPECTED, EDGE, PARIS, AUCTION}.

* Favorite Services: Services that the user wishes to consume. It takes values ​​from the set {DATA, VOICE, DATA & VOICE}.

The device capabilities are in a property file, they are read at startup and stored in a Record Store. This information can not be changed but will be presented to the user for information. Essentially, through an appropriate menu the user can inspect the capabilities of the device. For simplicity consider that a device may have a single SIM and every moment is connected to a single Network.
The characteristics of the user during the first start will be empty and will invited to complete before any other action. In all other cases, the user will be able to modify as desired through appropriate form. In case the change in the charging model has resulted in the latter vary in the way billing network, the application alerts the user. If the user accepts the change then the Mobile should be disconnected from the current Network and connect to another - if any.
The connection procedure to a Network can be done either automatically or by the user. This can be selected by the user through an appropriate screen. When starting the device is arranged to wait for the user to select the desired network. The list of Networks supported will be generated by the mobile application and will contain the network those supported by the device and suit to user preferences. When auto-connect, the device itself selects the Network which is closer to the user requirements.

The messages sent by the mobile are:

* ``` CONNECT <IMEI> <IMSI> <X> <Y> ```
e.g. ``` CONNECT 35395803-121326-9 310150123456789 10 10 ```. This command means that the user with IMSI 310150123456789 located at (10,10) wants to connect through the device IMEI 35395803-121326-9 to a Base Station.

* ```DISCONNECT <IMEI> ```
e.g. ``` DISCONNECT 35395803-121326-9 ```. This command means that the device IMEI 35395803-121326-9 has beeb disconnected from the current Base Station.

* ``` DISCOVER <IMEI> <X> <Y> ```
e.g. ``` DISCOVER 35395803-121326-9 10 10 ```. This command is sent from the Mobile to the Cognitive Pilot Channel's information software and asks for networks that serve a specific geographic area.

The messages received by the mobile are:

* ``` OK ``` in response to the ``` CONNECT/DISCONNECT ```.

* ``` OVERLOADED ``` in response to a ``` CONNECT ``` request when it can not be served due to Base Station load limits.

* ``` NO COVERAGE ``` in response to the ``` CONNECT ``` request when it can not be served due to non-coverage (based on the signaling, this message must not be sent).

* ``` DISCONNECT ``` that notifies the mobile disconnects from the Base Station.

* ``` PROFILES ``` updates Mobile for available Networks from the Cognitive Pilot Channel.

All the communication devices are made using sockets. For their implementation we have used Java Micro Edition.

## Cognitive Pilot Channel Management Software

This software is practically implementing the idea of ​​gathering and disseminating framework information through a specific channel. This functionality is implemented through two distinct steps:

* Search for available radio technologies.
* Send radio technologies available in the connected Mobiles upon request.

The first step does not require the sending of some specific messages since both network management entities and the terminal management entities send the required information. The following assumptions are necessary for the smooth operation of the system:

* If an entity management network does not send an alert message to 3t seconds thought that no longer works, and therefore is deleted from the Channel's memory management software.
* If there is a failure sending a message to a Mobile, the communication port is closed and the channel waits for a new connection. Essentially, it takes no action and awaits the Cell to manage the error.

The information about available technologies stored in a MySQL database. The management software communicates with the database via JDBC. For a DB Table is proposed - but its not compulsory- the use of the following format:

network_id, varchar (20),<br>
basestation_id, varchar (45),<br>
signalStrength, double,<br>
frequency, double,<br>
networkType, int,<br>
maxBitRate, double,<br>
guaranteedBitRate, double,<br>
net_load, double,<br>
provider, varchar (45),<br>
R, int,<br>
x int,<br>
y int,<br>
port, int,<br>
charging, int
