# Problem Statement
In 2018 alone, there have been 
[229 healthcare data breaches affecting 6.1M victims]( https://www.beckershospitalreview.com/cybersecurity/6-1m-healthcare-data-breach-victims-in-2018-5-of-the-biggest-breaches-so-far.html).
Such data security concerns are often the main reason that providers are hesitant to share healthcare data. Sharing patient information can help providers reduce readmissions, avoid medication errors, and even decrease duplicate testing.
Genetic studies, cancer/chronic disease registries, substance abuse, population health management, larger-scale analytics, epidemiology/disease tracking, and even interoperability for routine patient care in the emergency department 
[are all potential uses for data sharing](https://healthitsecurity.com/features/benefits-challenges-of-secure-healthcare-data-sharing).

<br/><br/>

![EHRouteLogoImage](https://i.imgur.com/7GLw3np.png)

<br/>

# EHR Route
EHR Route is an open source Electronic Health Records (EHR) sharing and storage solution that aims to prevent the most common causes of 
[EHRs data breaches](https://www.healthcareitnews.com/projects/biggest-healthcare-data-breaches-2018-so-far).
It closes gap between healthcare providers and patients, and ensures patients complete privacy and engagement in the process.

It utilizes [Public-key cryptography]( https://en.wikipedia.org/wiki/Public-key_cryptography)
(also known as asymmetric key encryption) And [Blockchain Technology]( https://en.wikipedia.org/wiki/Blockchain) 
using [Proof of authority]( https://en.wikipedia.org/wiki/Proof-of-authority) with a single validator at its core; Public-key cryptography enables Patients maximum engagement by giving them control of choosing who uses, edits, or shares their electronic health record. While blockchain ensures and verifies data integrity.

<br/>

### Private networks and Single Proof of Authority
EHR data is contained within the blocks of the blockchain; each block contains a single EHR, which represents a transaction. Multiple healthcare institutions would form a network, where all EHR data is shared among the nodes of the network and would all be members of a private blockchain network.
The networks are PoA based with a server as the only validator, this allows for complete privacy of the blockchain network, and authorization of its members.

<br/>

### Real-time Communication
Server-sent events are used for communication via an API between the nodes, authorized and routed by the server (It is planned to use WebRTC in the future) [Details and UML diagrams will be added to the wiki]. Since Server-sent events do not allow for binary data streaming, their only current advantage is that they enables the compression of large files by storing them on a cloud storage, and sending their download URI (which are deleted instantly upon download completion), which cannot be achieved by streaming.

<br/>

### Use case
Patients receive a public and private key-pair upon registration completion, that gets stored locally on their mobile devices using [EHR Route App](https://github.com/MuizMahdi/EHR-Route-Mobile), whenever a provider needs to access or edit a patient’s EHR, a consent request is sent to the patient, the patient could refuse or accept, if accepted, blocks are signed by the patient and then immediately broadcasted across the network, patients public keys could also be used to verify that they signed the block [Details and UML diagrams will be added to the wiki]. If they didn’t accept the request, then no block is broadcasted.

<br/>

#### The following UML use case diagram demonstrates the <b>basic</b> actions and use cases by healthcare providers and patients.

<br/><br/>

![UML_Diagram](https://dl.dropboxusercontent.com/s/kwjcssks29nzwgy/Provider_Patient_UmlDiagram.png)

