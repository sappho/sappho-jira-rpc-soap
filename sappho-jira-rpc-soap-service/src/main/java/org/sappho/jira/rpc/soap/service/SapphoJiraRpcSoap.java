package org.sappho.jira.rpc.soap.service;

import com.atlassian.jira.rpc.exception.RemoteException;

public interface SapphoJiraRpcSoap {

    public String login(String username, String password) throws RemoteException;

    public String getParentKey(String token, String issueKey) throws RemoteException;

    public String getMovedIssueKey(String token, String issueKey) throws RemoteException;

    public String getFieldChangeHistory(String token, String issueKey, String[] fieldNames) throws RemoteException;
}
