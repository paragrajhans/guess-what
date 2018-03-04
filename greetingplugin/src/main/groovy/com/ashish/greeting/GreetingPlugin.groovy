package com.ashish.greeting;
import org.gradle.api.Plugin
import org.gradle.api.Project


class GreetingPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.extensions.create('greeting', GreetingExtension)


        project.task('generateGreetings') {
            doLast {
                println "Greeting to :=> " + project.greeting.message
            }
        }


    }
}