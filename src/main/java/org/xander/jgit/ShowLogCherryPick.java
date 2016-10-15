package org.xander.jgit;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowLogCherryPick {
    private static String pathname;
    private Set<String> setOfHashesForAuxiliaryBranch;
    private Set<RevCommit> setOfCommitsOnMasterBranch;
    private Set<RevCommit> setOfCommitsOnAuxBranch;
    private static Repository repository;

    public static void main(String[] args) throws IOException, GitAPIException {
//        String workingDirectory = System.getProperty("user.dir");
//        System.out.println(workingDirectory);

        if (args[0] == null || args[1] == null || args[2] == null) {
            throw new RuntimeException("Please specify parameters correctly. (branch to compare with master, path to repository, path to ignore file)");
        }

        ShowLogCherryPick logCherryPick = new ShowLogCherryPick();

//        pathname = "D:\\tt";
        pathname = args[1];
        String pathToFile = args[2];

        Set<String> ignoreHashes = formHashesToIgnore(pathToFile);

        File workTree = new File(pathname);
        repository = logCherryPick.openGitRepository(workTree);

//        String branchName = "anotherBranch";
        String branchName = args[0];
        logCherryPick.workWithCustomBranch(branchName);
        logCherryPick.workWithMasterBranch();

        Set<String> cherryPickedCommitsOnMaster = formCherryPickedCommitsOnMaster(logCherryPick);

        formOutput(logCherryPick, cherryPickedCommitsOnMaster, ignoreHashes);

        repository.close();
    }

    private static Set<String> formHashesToIgnore(String pathToFile) throws IOException {
        File ignoreFile = new File(pathToFile);
        Set<String> ignoreHashes = new HashSet<>();
        BufferedReader br = new BufferedReader(new FileReader(ignoreFile));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                ignoreHashes.add(line);

                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        } finally {
            br.close();
        }
        return ignoreHashes;
    }

    private static Set<String> formCherryPickedCommitsOnMaster(ShowLogCherryPick logCherryPick) {
        Set<String> cherryPickedCommitsOnMaster = new HashSet<>();
        Pattern pattern = Pattern.compile(" ([0-9a-fA-F]+)[)]\\s*$");
        for (RevCommit commit : logCherryPick.setOfCommitsOnMasterBranch) {
            Matcher matcher = pattern.matcher(commit.getFullMessage());
            if (matcher.find()) {
                cherryPickedCommitsOnMaster.add(matcher.group(1));
            }
        }
        return cherryPickedCommitsOnMaster;
    }

    private static void formOutput(ShowLogCherryPick logCherryPick, Set<String> cherryPickedCommitsOnMaster, Set<String> ignoreHashes) {
        Set<String> notCherryPickedCommitsOnCustomBranch = new HashSet<>();
        notCherryPickedCommitsOnCustomBranch.addAll(logCherryPick.setOfHashesForAuxiliaryBranch);
        notCherryPickedCommitsOnCustomBranch.removeAll(cherryPickedCommitsOnMaster);

        Set<String> cherryPickedCommitsOnMasterWithIgnore = new HashSet<>();
        cherryPickedCommitsOnMasterWithIgnore.addAll(notCherryPickedCommitsOnCustomBranch);
        cherryPickedCommitsOnMasterWithIgnore.removeAll(ignoreHashes);

        System.out.println("\n---\nCommits that were not cheery picked onto the master branch are added to outputResult.log file: \n\n");

        StringBuilder sb = new StringBuilder();
        sb.append("\n---\nFollowing commits were not cheery picked onto the master branch: \n\n");
        for (RevCommit commitOnBranch : logCherryPick.setOfCommitsOnAuxBranch) {
            for (String notCherryPickedCommit : cherryPickedCommitsOnMasterWithIgnore)
                if (commitOnBranch.getName().equals(notCherryPickedCommit)) {
                    sb.append("------------\nHash - " + commitOnBranch.getName());
                    sb.append("\nAuthor - " + commitOnBranch.getAuthorIdent().getName() +
                            "\nEmail - " + commitOnBranch.getAuthorIdent().getEmailAddress() +
                            "\nDate - " + commitOnBranch.getAuthorIdent().getWhen() +
                               "\nCommit message - " + commitOnBranch.getFullMessage() + "------------\n");
                }
        }
        writeResultsToFile(sb);
    }

    private static void writeResultsToFile(StringBuilder sb) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("outputResult", "UTF-8");
            writer.println(sb.toString());
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    public Repository openGitRepository(File pathToRepo) throws IOException {
        Repository repository = new FileRepositoryBuilder().setWorkTree(pathToRepo).build();
        return repository;
    }

    private void workWithCustomBranch(String branchName) throws GitAPIException, IOException {
        switchToBranch(branchName);
        setOfHashesForAuxiliaryBranch = new HashSet<>();
        setOfHashesForAuxiliaryBranch = getCommitHashesForBranch();
    }

    private void workWithMasterBranch() throws GitAPIException, IOException {
        final String masterBranch = "master";
        switchToBranch(masterBranch);
        getCommitMessagesForMasterBranch();
    }

    private void switchToBranch(String branchName) throws IOException, GitAPIException {
        Git git = Git.open(new File(pathname));
        git.checkout().setName(branchName).call();
    }

    private Set<String> getCommitHashesForBranch() throws IOException {
        RevWalk rw = new RevWalk(repository);
        AnyObjectId HEAD = repository.resolve("HEAD");

        rw.markStart(rw.parseCommit(HEAD));
        Iterator<RevCommit> it = rw.iterator();

        Set<String> setOfHashes = new HashSet<>();
        setOfCommitsOnAuxBranch = new HashSet<>();

        while (it.hasNext()) {
            RevCommit commit = it.next();
            setOfHashes.add(commit.getName());

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis((long) commit.getCommitTime() * 1000);

            if (c.get(Calendar.YEAR) >= 2014 && c.get(Calendar.MONTH) >= 1 && c.get(Calendar.DAY_OF_MONTH) >= 3) {
                setOfCommitsOnAuxBranch.add(commit);
            }

        }
        rw.dispose();

        return setOfHashes;
    }

    private void getCommitMessagesForMasterBranch() throws IOException {
        RevWalk rw = new RevWalk(repository);
        AnyObjectId HEAD = repository.resolve("HEAD");

        rw.markStart(rw.parseCommit(HEAD));
        Iterator<RevCommit> it = rw.iterator();

        Set<String> setOfMessageHashes = new HashSet<>();
        setOfCommitsOnMasterBranch = new HashSet<>();

        while (it.hasNext()) {
            RevCommit commit = it.next();
            setOfMessageHashes.add(commit.getFullMessage());

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis((long) commit.getCommitTime() * 1000);

            if (c.get(Calendar.YEAR) >= 2014 && c.get(Calendar.MONTH) >= 1 && c.get(Calendar.DAY_OF_MONTH) >= 3) {
                setOfCommitsOnMasterBranch.add(commit);
            }
        }
        rw.dispose();
    }
}
