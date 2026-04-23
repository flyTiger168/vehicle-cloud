---
name: github-issue-puller
description: 从 GitHub 仓库拉取 Open Issue 列表，查看指定 Issue 的详情。当用户提到"拉取 issue"、"查看 issue"、"issue 列表"、"GitHub issue"、"待办 issue"时自动激活。
---

# GitHub Issue Puller

从 GitHub 仓库拉取 Open Issue 列表并查看指定 Issue 详情，使用 `gh` CLI 完成所有操作。

## 前置条件

- 已安装 [GitHub CLI (`gh`)](https://cli.github.com/)
- 已通过 `gh auth login` 完成认证

## 工作流程

### 1. 确定目标仓库

优先级：
1. 用户明确指定的 `owner/repo`
2. 当前工作目录的 Git remote（运行 `gh repo view --json nameWithOwner -q .nameWithOwner`）

如果都无法获取，请求用户提供仓库地址。

### 2. 拉取 Open Issue 列表

```bash
gh issue list --repo <owner/repo> --state open --limit 30
```

常用筛选参数：
- `--label <label>` — 按标签筛选
- `--assignee <user>` — 按指派人筛选
- `--search <query>` — 按关键词搜索
- `--limit <n>` — 限制返回数量（默认 30）

以表格形式向用户展示结果，包含：Issue 编号、标题、标签、指派人。

### 3. 查看指定 Issue 详情

```bash
gh issue view <issue-number> --repo <owner/repo>
```

向用户展示：标题、状态、作者、标签、指派人、正文内容及评论摘要。

如需 JSON 格式获取结构化数据：
```bash
gh issue view <issue-number> --repo <owner/repo> --json title,state,author,labels,assignees,body,comments
```

## 输出格式

向用户汇报时使用简洁的 Markdown 表格（列表）和摘要，避免输出原始 JSON。

## 错误处理

- `gh` 未安装 → 提示用户安装
- 认证失败 → 提示运行 `gh auth login`
- 仓库不存在或无权限 → 提示检查仓库地址和权限
