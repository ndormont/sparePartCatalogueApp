Feature: myfeature
Scenario: myfirstscenario
Given I'm logged in as "sy"
And I open an editor "myeditor" from table "Kunde:Kunde" with command "NEW" for record ""
And I set field "such" to "CUCUMBER"
When I save the current editor
Then field "such" has value "CUCUMBER"
