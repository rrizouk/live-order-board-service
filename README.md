# live-oder-board

## Technology stack
- Java8

## Technical notes and assumptions
- no third party libraries were used to keep things simple.
- Summary information objects encapsulates the logic for the information to be displayed on the live board e.g. merging same prices and ordering
so as not to clutter the service class and no assumptions were made about data format hence getOrdersSummary returns a list of objects
- the tests are using the same data set so as to make things easy for the reviewer

## Building and testing the app
- ensure java 8 and maven are installed installed
- run 'mvn clean test' command for tests
- run 'mvn clean package' command to create jar file


