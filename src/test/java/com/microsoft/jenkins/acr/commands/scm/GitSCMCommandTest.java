/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.jenkins.acr.commands.scm;

import com.microsoft.jenkins.acr.common.scm.GitSCMRequest;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;


public class GitSCMCommandTest extends AbstractSCMTest<GitSCMCommandTest.Request> {

    @Test
    public void commonTest() throws Exception {
        String repo = "https://github.com/Azure/azure-acr-plugin.git";
        String branch = "master";
        String path = null;
        String url = getSCMUrl(new Request(repo, branch, path));
        Assert.assertEquals(repo + "#" + branch, url);
    }

    @Test
    public void pathTest() throws Exception {
        String repo = "https://github.com/Azure/azure-acr-plugin.git";
        String branch = null;
        String path = "src";
        String url = getSCMUrl(new Request(repo, branch, path));
        Assert.assertEquals(repo + ":" + path, url);
    }

    @Test
    public void fullTest() throws Exception {
        String repo = "https://github.com/Azure/azure-acr-plugin.git";
        String branch = "master";
        String path = "src";
        String url = getSCMUrl(new Request(repo, branch, path));
        Assert.assertEquals(repo + "#" + branch + ":" + path, url);
    }

    @Test
    public void repoCompleteTest() throws Exception {
        String repo = "https://github.com/Azure/azure-acr-plugin";
        String branch = "master";
        String path = null;
        String url = getSCMUrl(new Request(repo, branch, path));
        Assert.assertEquals(repo + ".git#" + branch, url);
    }

    @Test
    public void repoResolveTest() throws Exception {
        String repo = "https://github.com/Azure/azure-acr-plugin/";
        String branch = "master";
        String path = null;
        String url = getSCMUrl(new Request(repo, branch, path));
        Assert.assertEquals("https://github.com/Azure/azure-acr-plugin.git#" + branch, url);
    }

    @Override
    protected AbstractSCMCommand getCommand() throws IllegalAccessException, InstantiationException {
        return GitSCMCommand.class.newInstance();
    }


    class Request extends AbstractSCMRequest implements GitSCMCommand.IGitSCMData, GitSCMRequest {
        @Getter
        private final String gitRepo;
        @Getter
        private final String gitRefspec;
        @Getter
        private final String gitPath;


        public Request(String repo, String refspec, String path) {
            this.gitRepo = repo;
            this.gitRefspec = refspec;
            this.gitPath = path;
        }

        @Override
        public GitSCMRequest getGitSCMRequest() {
            return this;
        }
    }
}
