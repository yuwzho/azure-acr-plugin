/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.jenkins.acr.util;

import com.microsoft.jenkins.acr.common.DockerTaskRequest;
import com.microsoft.jenkins.acr.common.Platform;
import com.microsoft.jenkins.acr.descriptor.BuildArgument;
import com.microsoft.jenkins.acr.descriptor.Image;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UtilTest {
    @Test
    public void toJsonTest() {
        List<String> imageList = new ArrayList<>();
        imageList.add("1");
        imageList.add("2");
        DockerTaskRequest request = DockerTaskRequest.builder()
                .platform(new Platform("Linux", "AMD64", "V6"))
                .localDir("gitrepo")
                .buildArguments(new BuildArgument[]{
                        new BuildArgument("key", "secret", false)
                })
                .imageNames(imageList)
                .noCache(false)
                .build();
        Assert.assertEquals("{\"localDir\":\"gitrepo\"," +
                "\"imageNames\":[\"1\",\"2\"]," +
                "\"buildArguments\":[{\"key\":\"key\",\"secrecy\":false}]," +
                "\"noCache\":false," +
                "\"timeout\":0," +
                "\"platform\":{\"os\":\"Linux\",\"architecture\":\"AMD64\",\"variant\":\"V6\"}," +
                "\"canceled\":false}", Util.toJson(request));
    }

    @Test
    public void verifyLocation() {
        Assert.assertTrue(Util.verifyGitUrl(null));
        Assert.assertTrue(Util.verifyGitUrl(""));
        Assert.assertTrue(Util.verifyGitUrl("https://github.com/Azure/azure-acr-plugin.git"));
        Assert.assertTrue(Util.verifyGitUrl("https://github.com/Azure/azure-acr-plugin"));
        Assert.assertTrue(Util.verifyGitUrl("https://github.com/Azure/azure-acr-plugin/"));
        Assert.assertTrue(Util.verifyGitUrl("http://github.com/Azure/azure-acr-plugin/"));
        Assert.assertFalse(Util.verifyGitUrl("git@github.com:Azure/azure-acr-plugin.git"));
    }

    @Test
    public void toStringArrayTest() {

        String[] src = new String[]{"a", "b", "c"};
        List<Image> list = new ArrayList<>();
        for (String i : src) {
            list.add(new Image(i));
        }

        String[] result = Util.toStringArray(list);
        Assert.assertEquals(3, result.length);
        Assert.assertEquals("a", result[0]);
        Assert.assertEquals("b", result[1]);
        Assert.assertEquals("c", result[2]);

        Assert.assertEquals(0, Util.toStringArray(null).length);
    }

    @Test
    public void normalizeFilenameTest() {
        Assert.assertEquals("C://a/b/d", Util.normalizeFilename("C:\\\\a\\b\\d"));
        Assert.assertEquals("C://a/b/d", Util.normalizeFilename("C://a/b/d"));
    }

    @Test
    public void concatPathTest() {
        Assert.assertEquals("/home/user/file", Util.concatPath("/home/user", "file"));
        Assert.assertEquals("C://a/b", Util.concatPath("/home/user", "C://a/b"));
    }

    @Test
    public void getFilenameTest() {
        Assert.assertEquals("file", Util.getFileName("/c/d/e/file"));
        Assert.assertEquals("file", Util.getFileName("file"));
        Assert.assertEquals("file", Util.getFileName("C:\\\\d\\file"));
    }
}
