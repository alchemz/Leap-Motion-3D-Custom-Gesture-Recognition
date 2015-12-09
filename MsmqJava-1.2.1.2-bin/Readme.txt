created Fri, 25 Feb 2005  15:31
updated Sat, 27 Mar 2010  12:36

This is MsmqJava, a JNI library that allows Java to interact with MSMQ,
the built-in message queue on Windows.

This can facilitate interoperability between Java and .NET applications,
or Java and C/C++ applications, on Windows, using the MSMQ queue as the
intermediary.





===========================================================

To Use the Library
------------------

    Pre-requisites:
    --------------

    To Use the library:
    - - - - - - - - - - - - -

    1. Windows and MSMQ

    You need Windows 2000, or later, either a client or server OS.
    (XP, Vista, 2003, 2008) Also, you need to have MSMQ installed
    and running.

    To install on Windows XP, MSMQ, Start...Control
    Panel....Add/Remove Programs.  and then select "Add/Remove
    Windows Components" from the left hand side.  Check the "Message
    Queuing" box to and click "Next" to install MSMQ.  On other OS,
    installing MSMQ may be different.

    To insure MSMQ is running, click
    Start...Control Panel... Administrative Tools...Services.  Then
    scroll down to find "Message Queuing".  Right click the entry to
    insure MSMQ is started.


    2. JDK 1.5 or later

    It works with Sun JDK 1.5, Sun JDK 1.6, and IBM JDK 5.





To use the library, unpack the binary package, which contains a DLL and
a JAR (as well as the docunmentation).

Write a Java application that uses the ionic.msmq classes within the
JAR.

Be sure the MsmqJava.jar is on your
classpath, and the MsmqJava.dll is on your PATH.

Compile your java application.

Run it.




===========================================================



To Build the Library
--------------------




Pre-requisites:
--------------

    To Build the library:
    - - - - - - - - - - - - -

    1.  Visual-C++

    The Microsoft Visual C++ 2003 Toolkit , or Visual Studio 2003,
    or later.  The former is downloadable, for free, from
    http://msdn.microsoft.com/visualc/vctoolkit2003/ . The latter is
    a commercial product, you'll jave to buy it.

    You need the C++ compiler to compile the JNI layer for the
    Java application.



    2. The Microsoft Windows SDK

    The Platform SDK is needed for the MQ Runtime library (mqrt.lib)
    and a few header files.
    http://www.microsoft.com/msdownload/platformsdk/sdkupdate

    You just need the Core SDK.  The platform SDK is another free
    download.  If you have Visual Studio 2003 installed, you already
    have the necessary stuff.  If you installed the VC++ Toolkit,
    you may not have the proper stuff.




    Pre-requisites:
    --------------

    To Use the .NET Sample app:
    - - - - - - - - - - - - -

    1 .NET Framework v2.0 or later

    For compiling and running the .NET code.  You don't actually
    need the SDK, just the .NET redistributable.




Building
- - - - - -

There are two primary directories:

  library - builds the MsmqJava.dll JNI library, the Jar,
            and the Javadoc documentation.

  clients - builds clients that use the DLL and JAR



In the library directory, there's a makefile. to use it:

  cd library
  nmake CONFIG=Debug install

Targets:

    install - produces binaries and documentation, and inserts
              them into an "install" directory in the parent.
    doc     - produces javadoc documentation.


There's also a makefile in the clients directory:

  cd clients
  nmake CONFIG=Debug

You can also use CONFIG=Release in either of those directories.






Testing it
------------

This project includes two test clients, one written in Java and one
in C#.

Each app is a text-based console application.  They are
functionally equivalent; they can each create, open, close or
delete queues, and can send, receive or peek messages.  The C#
application connects to MSMQ via the System.Messaging classes.  The
Java application connects to MSMQ via the library produced by this
project.

By sending from the .NET app, and receiving in the Java app, or
vice versa, you can easily show interop between .NET and Java
via MSMQ.

To run the java application, you can use the runJavaClient.cmd
command file. This sets up the necessary classpath and path. You
can run the .NET version without any extra setup.

These are just example apps.  You could embed the Java library
into any application, client or server side, using a GUI or not.







Notes:
--------

1. On Transactions

By default the TestClient app calls sendMessage
with MQ_NO_TRANSACTION. If the queue is a transactional queue,
then the sendMessage() will appear to succeed, but the message
will never appear in the queue.  If you let the TestClient app
create its own queue, it creates it as a non-transactional
queue, and everything is fine.  However, if you have created the
queue via the MSMQ administrative interface, and you have
specified that the queue is transactional, you may see this
behavior.  To correct this, use a non-transactional queue.


2. On Sending and Receiving

This may seem obvious to some, but in the test client apps, you
need to open the queue before attempting to send or receive
messages.

The TestClient.java app, via the nativeOpenQueue() method from
the MsmqQueueNativeMethods.cpp module, opens the queue twice:
once for receiving and once for sending.  You don't need to do
it this way if your app doesn't need to do both sending and
receiving from the same queue.  There are other nativeOpenQueue*
methods in the MsmqQueueNativeMethods.cpp module.  You can use
whichever is most appropriate or modify it to suit your
situation.


3. Remote queues

You can use the TestClients on different machines, connected to
the same MSMQ, to illustrate cross-network communication.  open,
receive, and send should all work.  MSMQ does not allow creation
of remote private queues.


