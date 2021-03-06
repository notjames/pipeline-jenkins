import jenkins.*
import hudson.*
import hudson.model.*
import jenkins.model.*
import java.util.List;
import java.util.ArrayList;
import org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever;
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration;
import org.jenkinsci.plugins.github_branch_source.GitHubSCMSource;
import org.jenkinsci.plugins.github_branch_source.BranchDiscoveryTrait;
import org.jenkinsci.plugins.github_branch_source.ForkPullRequestDiscoveryTrait;
import jenkins.scm.api.trait.SCMSourceTrait;
import jenkins.scm.api.mixin.ChangeRequestCheckoutStrategy;

// shared library instance
def globalLibsDesc = Jenkins.getInstance()
  .getDescriptor("org.jenkinsci.plugins.workflow.libs.GlobalLibraries")

// setup some github SCM traits
List<SCMSourceTrait> traits = new ArrayList<>()
traits.add(new BranchDiscoveryTrait(false, false))
traits.add(
  new ForkPullRequestDiscoveryTrait(
    EnumSet.of(ChangeRequestCheckoutStrategy.MERGE), 
    new ForkPullRequestDiscoveryTrait.TrustPermission()))

// setup github SCM
GitHubSCMSource githubSCM = new GitHubSCMSource(
  java.util.UUID.randomUUID().toString(),
  "{{ .Values.github.apiUrl }}",
  "github-access", 
  "github-access",
  "{{ .Values.github.pipelineOrg }}",
  "{{ .Values.github.pipelineRepo }}")
githubSCM.setTraits(traits)

// scm retriever instance
SCMSourceRetriever retriever = new SCMSourceRetriever(githubSCM)

// create pipeline library config
LibraryConfiguration pipelineConfig = new LibraryConfiguration(
  "pipeline", 
  retriever)
pipelineConfig.setDefaultVersion("master")
pipelineConfig.setImplicit(true)
pipelineConfig.setAllowVersionOverride(true)
pipelineConfig.setIncludeInChangesets(true)

// set libraries
globalLibsDesc.get().setLibraries([pipelineConfig])