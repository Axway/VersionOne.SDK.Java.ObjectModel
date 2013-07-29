## Getting Started

Add a reference to the VersionOne object model assembly, VersionOne.ObjectModel.jar. Next, add the following using statement(s) to your source file:

```
// for the basic object model
import com.versionone.om.*;
// if you need to use filtering in getting filtered subsets of relations or collections of assets
import com.versionone.om.filters.*;
```

The first step is to create a connection to the VersionOne system with a V1Instance object (com.versionone.om.V1Instance), passing VersionOne application connection information into its' constructor:

```
V1Instance v1 = new V1Instance( url, username, password );
```
		
Once you have a valid V1Instance, you can navigate the entire VersionOne system. For example, root-level projects are available by calling getProjects() on the V1Instance instance.

```
for(Project project : v1.getProjects())  {
	System.out.writeline( project.getName());
}
```
