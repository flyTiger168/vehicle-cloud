---
name: github-issue-puller
description: 从 GitHub 仓库拉取 Open Issue 列表并展示，也支持查看指定 Issue 的详细信息。当用户提到"拉取 issue"、"查看 issue"、"open issues"、"待办 issue"、"GitHub 问题列表"、"issue 详情"、"issue #N"时自动激活。
---

# GitHub Issue Puller

从当前项目关联的 GitHub 仓库拉取 Open 状态的 Issue 列表，并以结构化方式展示。

## 前置条件

- 已安装 `gh` CLI 并完成认证（`gh auth status`）
- 当前工作目录是一个 Git 仓库，且已关联 GitHub remote

## 工作流程

### 1. 检测仓库信息

运行以下命令获取当前仓库的 owner/repo：

```bash
gh repo view --json nameWithOwner -q '.nameWithOwner'
```

如果失败，提示用户检查 GitHub remote 配置。

### 2. 拉取 Open Issue 列表

默认拉取所有 Open Issue（最多 100 条）：

```bash
gh issue list --state open --limit 100 --json number,title,labels,assignees,createdAt,url
```

### 3. 展示结果

将结果以**表格**形式展示给用户，包含以下列：

| 列 | 说明 |
|----|------|
| `#` | Issue 编号 |
| 标题 | Issue 标题 |
| 标签 | 所有 label 名称，逗号分隔 |
| 负责人 | assignee 的 login，逗号分隔 |
| 创建时间 | 格式化为 `YYYY-MM-DD` |

表格按 Issue 编号**升序**排列。

末尾附上汇总信息：`共 N 个 Open Issue`。

## 可选参数

用户可以指定筛选条件，通过追加 `gh issue list` 参数实现：

- **按标签筛选**：`--label "bug"`
- **按负责人筛选**：`--assignee "@me"`
- **限制数量**：`--limit 30`

示例：用户说"拉取我负责的 bug issue"，则运行：

```bash
gh issue list --state open --label "bug" --assignee "@me" --json number,title,labels,assignees,createdAt,url
```

# 查看指定 Issue 详情

当用户指定 Issue 编号（如"查看 issue #5"、"issue 12 详情"）时，拉取该 Issue 的完整信息：

```bash
gh issue view <number> --json number,title,body,state,labels,assignees,comments,createdAt,updatedAt,url
```

### 展示格式

按以下结构展示：

```
# #<number> <title>

状态: <state>  |  标签: <labels>  |  负责人: <assignees>
创建时间: <createdAt>  |  更新时间: <updatedAt>
链接: <url>

---
## 描述
<body 内容，保留 Markdown 格式>

---
## 评论 (N 条)

**@<login>** · <createdAt>
<comment body>
```

- 如果 `body` 为空，显示 *「无描述」*
- 如果无评论，显示 *「暂无评论」*
- 时间格式化为 `YYYY-MM-DD HH:mm`

## 错误处理

- `gh` 未安装 → 提示用户安装：`brew install gh`
- 未认证 → 提示运行 `gh auth login`
- 非 GitHub 仓库 → 提示用户确认 remote 配置
- Issue 编号不存在 → 提示用户确认编号是否正确
