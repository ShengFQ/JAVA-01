####学习笔记
<pre>
jmap -heap 68430
Server compiler detected.
JVM version is 25.77-b03

using thread-local object allocation.
Parallel GC with 4 thread(s)

Heap Configuration:
   MinHeapFreeRatio         = 0
   MaxHeapFreeRatio         = 100
   MaxHeapSize              = 2147483648 (2048.0MB)
   NewSize                  = 44564480 (42.5MB)
   MaxNewSize               = 715653120 (682.5MB)
   OldSize                  = 89653248 (85.5MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 21807104 (20.796875MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 17592186044415 MB
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
PS Young Generation
Eden Space:
   capacity = 681050112 (649.5MB)
   used     = 564764576 (538.6014709472656MB)
   free     = 116285536 (110.89852905273438MB)
   82.92555364853975% used
From Space:
   capacity = 11010048 (10.5MB)
   used     = 10835488 (10.333526611328125MB)
   free     = 174560 (0.166473388671875MB)
   98.41453915550595% used
To Space:
   capacity = 17825792 (17.0MB)
   used     = 0 (0.0MB)
   free     = 17825792 (17.0MB)
   0.0% used
PS Old Generation
   capacity = 85458944 (81.5MB)
   used     = 26213432 (24.99907684326172MB)
   free     = 59245512 (56.50092315673828MB)
   30.67371391811254% used

24163 interned Strings occupying 3072112 bytes.

===================================


jinfo 68430
Attaching to process ID 68430, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.77-b03
Java System Properties:

java.runtime.name = Java(TM) SE Runtime Environment
java.vm.version = 25.77-b03
sun.boot.library.path = /Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib
java.protocol.handler.pkgs = org.springframework.boot.loader
gopherProxySet = false
java.vendor.url = http://java.oracle.com/
java.vm.vendor = Oracle Corporation
path.separator = :
java.rmi.server.randomIDs = true
file.encoding.pkg = sun.io
java.vm.name = Java HotSpot(TM) 64-Bit Server VM
sun.os.patch.level = unknown
sun.java.launcher = SUN_STANDARD
user.country = CN
user.dir = /Users/sheng
java.vm.specification.name = Java Virtual Machine Specification
PID = 68430
java.runtime.version = 1.8.0_77-b03
java.awt.graphicsenv = sun.awt.CGraphicsEnvironment
os.arch = x86_64
java.endorsed.dirs = /Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/endorsed
line.separator = 

java.io.tmpdir = /var/folders/q3/1hjk6bf54gg07y626mqms12m0000gn/T/
java.vm.specification.vendor = Oracle Corporation
os.name = Mac OS X
sun.jnu.encoding = UTF-8
java.library.path = /Users/sheng/Library/Java/Extensions:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java:.
spring.beaninfo.ignore = true
java.specification.name = Java Platform API Specification
java.class.version = 52.0
sun.management.compiler = HotSpot 64-Bit Tiered Compilers
os.version = 10.13.6
user.home = /Users/sheng
user.timezone = Asia/Shanghai
catalina.useNaming = false
java.awt.printerjob = sun.lwawt.macosx.CPrinterJob
file.encoding = UTF-8
@appId = eurka-server
java.specification.version = 1.8
catalina.home = /private/var/folders/q3/1hjk6bf54gg07y626mqms12m0000gn/T/tomcat.2066157831224450024.8761
user.name = sheng
java.class.path = /Users/sheng/Desktop/geer2-springcloud-service-1.0-SNAPSHOT.jar
java.vm.specification.version = 1.8
sun.arch.data.model = 64
sun.java.command = /Users/sheng/Desktop/geer2-springcloud-service-1.0-SNAPSHOT.jar
java.home = /Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre
user.language = zh
java.specification.vendor = Oracle Corporation
user.language.format = en
awt.toolkit = sun.lwawt.macosx.LWCToolkit
java.vm.info = mixed mode
java.version = 1.8.0_77
java.ext.dirs = /Users/sheng/Library/Java/Extensions:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/ext:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java
sun.boot.class.path = /Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/sunrsasign.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/classes
java.awt.headless = true
java.vendor = Oracle Corporation
catalina.base = /private/var/folders/q3/1hjk6bf54gg07y626mqms12m0000gn/T/tomcat.2066157831224450024.8761
file.separator = /
java.vendor.url.bug = http://bugreport.sun.com/bugreport/
sun.io.unicode.encoding = UnicodeBig
sun.cpu.endian = little
sun.cpu.isalist = 

VM Flags:
Non-default VM flags: -XX:CICompilerCount=3 -XX:InitialHeapSize=134217728 -XX:MaxHeapSize=2147483648 -XX:MaxNewSize=715653120 -XX:MinHeapDeltaBytes=524288 -XX:NewSize=44564480 -XX:OldSize=89653248 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseFastUnorderedTimeStamps -XX:+UseParallelGC 
</pre>