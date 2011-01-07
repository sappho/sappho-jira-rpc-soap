package org.sappho.jira.rpc.soap.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ofbiz.core.entity.GenericEntityException;
import org.sappho.jira.rpc.soap.common.FieldChange;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.changehistory.ChangeHistoryManager;
import com.atlassian.jira.issue.history.ChangeItemBean;
import com.atlassian.jira.rpc.auth.TokenManager;
import com.atlassian.jira.rpc.exception.RemoteException;
import com.thoughtworks.xstream.XStream;

public class SapphoJiraRpcSoapImpl implements SapphoJiraRpcSoap {

    private final TokenManager tokenManager;
    private final IssueManager issueManager;
    private final ChangeHistoryManager changeHistoryManager;

    public SapphoJiraRpcSoapImpl(TokenManager tokenManager, IssueManager issueManager,
            ChangeHistoryManager changeHistoryManager) {

        this.tokenManager = tokenManager;
        this.issueManager = issueManager;
        this.changeHistoryManager = changeHistoryManager;
    }

    public String login(String username, String password) throws RemoteException {

        return tokenManager.login(username, password);
    }

    public String getParentKey(String token, String issueKey) throws RemoteException {

        tokenManager.retrieveUserNoPermissionCheck(token);
        MutableIssue issue = issueManager.getIssueObject(issueKey);
        Issue parentIssue = issue.getParentObject();
        return parentIssue != null ? parentIssue.getKey() : null;
    }

    public String getMovedIssueKey(String token, String issueKey) throws RemoteException {

        tokenManager.retrieveUserNoPermissionCheck(token);
        Issue issue = null;
        try {
            issue = changeHistoryManager.findMovedIssue(issueKey);
        } catch (GenericEntityException e) {
        }
        return issue != null ? issue.getKey() : null;
    }

    public String getFieldChangeHistory(String token, String issueKey, String[] fieldNames) throws RemoteException {

        tokenManager.retrieveUserNoPermissionCheck(token);
        MutableIssue issue = issueManager.getIssueObject(issueKey);
        Map<String, List<FieldChange>> history = new HashMap<String, List<FieldChange>>();
        Calendar calendar = Calendar.getInstance();
        for (String fieldName : fieldNames) {
            List<FieldChange> fieldChanges = new ArrayList<FieldChange>();
            history.put(fieldName, fieldChanges);
            List<ChangeItemBean> changes = changeHistoryManager.getChangeItemsForField(issue, fieldName);
            for (ChangeItemBean change : changes) {
                calendar.setTimeInMillis(change.getCreated().getTime());
                fieldChanges.add(new FieldChange(calendar.getTime(), change.getFrom(), change.getTo()));
            }
        }
        return new XStream().toXML(history);
    }
}
