import javaposse.jobdsl.plugin.GlobalJobDslSecurityConfiguration
import jenkins.model.GlobalConfiguration

GlobalConfiguration.all().get(GlobalJobDslSecurityConfiguration.class).useScriptSecurity={{ .Values.master.dslScriptSecurity }}