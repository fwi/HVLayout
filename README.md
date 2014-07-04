##### HVLayout manager for Java Swing

Windows should be resizable and window contents should adapt to user preferred font size. 
For many people displaying text with a large font is not an option but a requirement to read information on screen 
(e.g. the ``ctrl-plus`` function in an internet browser is a necessity).
It should be relatively easy to program a window showing a form with Java Swing that respects both requirements.
HVLayout can help you reach these goals.

***Using HVLayout***

Components displayed on screen can be aligned Horizontally and Vertically.
Horizontally aligned components are put in a HBox and vertically aligned components are put in a VBox.
An HBox can be put in a VBox and vice versa (nesting).

A component is initially displayed using it's preferred size and grows if it has a larger maximum size
and shrinks if it has a smaller minimum size. Growing and shrinking is always relative to how 
much other components in the same window want to grow and shrink.
<br/>Default component sizes are stored in ``HVSize`` and to apply these sizes to a component 
``CSize`` (which has a fluent API) can be used (and extended if need).
For example, to create a button that does not grow or shrink:
<br/>``new CSize().set(new JButton("Example")).setFixedSize();``
<br/>and done.
<br/>``CForm`` (also with a fluent API) is available to further assist in building and updating forms/content panes.

**Check it out**

HVlayout is in the ``swing-hvlayout`` module of the multi-module Maven project in this repository,
but HVLayout has no dependencies and can be used separately from the other modules
(it could get it's own repository in the future).
<br/>To play with the demo screens (screenshots shown below), 
download the [demo bundle](https://github.com/fwi/HVLayout/raw/master/swing-demo/demo-build/swing-demo-0.1.1-project.zip) 
zip file, extract it and execute one of the "run..." files
(for Linux users: copy and paste the contents of one of the run-files to the command prompt and remove the "w" from "javaw").
<br/>To compile and build, checkout the complete repository (download as zip) and run ``mvn package`` in the main project (aggregator) directory.
<br/>The ``swing-demo`` project contains a couple of test and demonstration screens.
Have a look at the [source code](https://github.com/fwi/HVLayout/tree/master/swing-demo/src/main/java/nl/fw/swing/demo) to see if HVLayout is something you can use
and run one of the demo-windows to see how HVLayout shrinks and grows components. 
<br/>The ``swing-util`` project is irrelevant/under construction at the moment, but you may see something you like.
I'm refactoring code from my [old fwutil project](https://java.net/projects/fwutil/sources/svn/show/trunk/fwutil)
(where all this once started) in hope of building something useful (as time permits).

**Screenshots**

From the ``swing-demo`` project, some screenshots.
<br/>The ``AddressBookDemo`` with preferred size and minimum size:
<br/>![AddressBookDemo-pre-min](https://github.com/fwi/HVLayout/raw/master/swing-demo/screenshots/address-book-pref-min.png)
<br/>The ``AddressBookDemo`` with a bigger window:
<br/>![AddressBookDemo-max](https://github.com/fwi/HVLayout/raw/master/swing-demo/screenshots/address-book-max.png)
<br/>The ``HvlayoutTestUI`` with preferred size and minimum size:
<br/>![HvlayoutTestUI-pref-min](https://github.com/fwi/HVLayout/raw/master/swing-demo/screenshots/testui-pref-min.png)
<br/>The ``HvlayoutTestUI`` with a big font, right-to-left component orientation and a bigger window:
<br/>![HvlayoutTestUI-max](https://github.com/fwi/HVLayout/raw/master/swing-demo/screenshots/testui-max-reverse.png)
<br/>The ``RelativeToWindowSize`` with different sizes:
<br/>![RelativeToWindowSize-all](https://github.com/fwi/HVLayout/raw/master/swing-demo/screenshots/rel-to-window-all.png)
<br/>

Unrelated: I'm also maintaining [Yapool](https://code.google.com/p/yapool/) (a Java generic object pool including a database connection pool).
