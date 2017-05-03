Mundane MoneyDance Extension
===

## Development instructions

### Moneydance development kit

If on a POSIX system (linux, unix, Mac OS X), execute the following from the root of this project. It downloads and
places the extracted files at the right place.

    curl http://infinitekind-downloads.s3.amazonaws.com/moneydance-devkit-4.0.tar.gz | tar -zxv

Otherwise, do it manually:
- Download the Moneydance development kit following this link: [moneydance-devkit-4.0](http://infinitekind-downloads.s3.amazonaws.com/moneydance-devkit-4.0.tar.gz). The direct link may change, so refer to [Moneydance developper section](http://infinitekind.com/developer) on their website.
- Extract it at the root of this project under the same name (without the extension)

### Moneydance Class Path Environment Variable

In order to compile the extension, compiling against the development kit is not sufficient (the kit itself references
classes that are not part of the `JAR` file). In order for the `ant` script to include your actual installation of
_Moneydance_ in the classpath at compile time, you must create the `MONEYDANCE_CLASSPATH` environment variable on your
system. If you are on macOS and have installed Moneydance normally (in the `/Applications` directory), you do not have
to set the environment variable since its default is that folder (see `build.gradle`, where variable 
`moneydanceInstalled` is set, for details).

### Generate keys

_Moneydance_ needs the final extension file to be signed (`.jar` becomes `.mxt`). To do so, it needs two key files. To
generate them, simply execute the following command and enter a pass phrase you will remember. It will be needed every
time you build the extension.

    ./gradlew genkeys
    
### Building the extension

To perform a complete build, package (`jar`) and sign (`mxt`), execute the following command and enter the pass phrase
you used when executing `genkeys`.

    ./gradlew mxt

The result can be found in `out/mundane.mxt`. That is the extension itself, that needs to be installed in _Moneydance_.

## Develop with IntelliJ IDEA

NB: This was written for IntelliJ IDEA `2017.1`.

### Getting started

#### Create the project and set up dependencies

- Open the root folder of the project in IntelliJ.
- Menu `File` -> `Project Structure`
    - Section `Project Settings` -> `Libraries`
        - Click the `+` button (2nd column) and chose `Java`
        - Browse to the installation folder of Moneydance and select the sub-folder with all the JAR files in. Typically, this will be:
            - on a Mac: `/Applications/Moneydance.app/Contents/Java`
            - on Windows: `C:\Program Files\Moneydance\jars`
            - on Linux: well ... wherever you installed it.
        - Add the documentation, using the `+` button at the bottom of the right section:
        	- From the project root: `moneydance-devkit-4.0/doc`
        	- This should create a `JavaDocs` section below `Classes`.
        - Rename the newly added library to `Moneydance Installed` and click "OK" (bottom right).


### Debugging and running directly from the IDE

Taken from a [forum post](http://help.infinitekind.com/discussions/moneydance-development/824-debugging-moneydance-extensions-in-eclipse) at infinitekind.

#### In a nutshell

Add a Run/Debug Configuration of type `Application` with the following settings:

- `Main class`: `Moneydance`
- `Working directory`:
    - on Windows: `C:\Program Files\Moneydance\jars`
    - on Mac OS X: `/Applications/Moneydance.app/Contents/Java`

The first time, you need to:

- build the extension from the command line (with `ant`)
- start the application from IntelliJ (run or debug)
- install the extension (the generated `.mxt` file, in folder `dist` of the project)

The next times, the modifications will automatically be taken into account when started from IntelliJ. No need to build and install the extension at every change.


## Note on document folder and configuration files

### Mac OS X

Configuration file when launched normally, from the Moneydance Mac OS X ".app":

    ~/Library/Containers/com.infinitekind.MoneydanceOSX/Data/Library/Application Support/Moneydance

Configuration file when launched manually (ie: from IntelliJ in 'debug'):

    ~/Library/Application Support/Moneydance

In both cases, the configuration file is `config.dict`. This facilitates the development since
there can be 2 sets of settings, default data file and extensions. You will not mess with your
normal files and configuration while developing.

## Original README from Infinite Kind

Please do read the README provided with the Moneydance Extension
development kit: ```moneydance-devkit-4.0/README.txt```
