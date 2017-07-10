# MegaDrumManagerFX

### Summary
MegaDrumManagerFX is a utility for managing the settings
of the MegaDrum drum module.  For more information about MegaDrum, 
including how to build or buy one for yourself, see 
http://megadrum.info

### Workspace Pre-Requisites
* JDK 1.8
* IntelliJ or Eclipse IDE

### Setting Up IntelliJ
1. Clone the repo
2. Open IntelliJ and select 'File > New > Project from existing sources'
3. Select 'eclipse' as the template to import from
4. Open 'File > Project Structure', go to the Dependencies tab
and add the following via Maven:
    * org.ow2.util.bundles:commons-collections-3.2.1:1.0.0
    * commons-configuration:commons-configuration:1.8
5. Setting a debug configuration
    * Go to 'edit configurations'
    * Add a new 'Application' type configuration
    * Enter 'info.megadrum.managerfx.Main' as the main class





