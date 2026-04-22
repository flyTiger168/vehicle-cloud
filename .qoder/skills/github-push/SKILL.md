---
name: github-push
description: Create public GitHub repositories, create branches, commit and push local code to GitHub using the gh CLI. Use when the user wants to publish code to GitHub, create a new remote repo, push to a branch, or set up a GitHub remote for an existing local project.
---

# GitHub 推送工作流

将本地仓库推送到 GitHub：创建远程仓库、管理分支、提交并推送代码。需要安装并认证 `gh` CLI。

## 前置检查

在任何操作之前，先验证 gh CLI 是否可用且已认证：

```bash
gh auth status
```

如果未认证，引导用户运行：

```bash
gh auth login
```

## 工作流 1：创建公开仓库并推送本地代码

适用于本地项目尚未关联 GitHub 远程仓库的场景：

```bash
# 1. 如果还不是 git 仓库，先初始化（已有则跳过）
git init

# 2. 在 GitHub 上创建远程仓库并关联为 origin
gh repo create <仓库名> --public --source=. --remote=origin

# 3. 暂存、提交并推送
git add .
git commit -m "Initial commit"
git push -u origin main
```

**`gh repo create` 常用参数：**

| 参数 | 用途 |
|------|------|
| `--public` | 创建公开仓库 |
| `--private` | 创建私有仓库 |
| `--source=.` | 使用当前目录作为源码 |
| `--remote=origin` | 设置远程仓库名称 |
| `--description "..."` | 添加仓库描述 |

## 工作流 2：创建新分支并推送

```bash
# 1. 创建并切换到新分支
git checkout -b <分支名>

# 2. 暂存并提交更改
git add .
git commit -m "<提交信息>"

# 3. 推送分支到远程
git push -u origin <分支名>
```

## 工作流 3：推送到已有远程仓库

```bash
# 1. 暂存更改
git add .

# 2. 提交（使用描述性信息）
git commit -m "<提交信息>"

# 3. 推送到当前分支
git push
```

## 提交信息规范

遵循约定式提交（Conventional Commits）格式：

```
<类型>(<范围>): <简短描述>

<可选的正文>
```

类型：`feat`（新功能）、`fix`（修复）、`docs`（文档）、`refactor`（重构）、`test`（测试）、`chore`（杂项）、`build`（构建）

## 错误处理

- **"remote origin already exists"** → 使用 `git remote set-url origin <新地址>` 更新远程地址
- **"gh: not found"** → 引导用户安装：`brew install gh`（macOS）或访问 https://cli.github.com
- **"not authenticated"** → 运行 `gh auth login` 进行认证
- **推送被拒绝（non-fast-forward）** → 先拉取：`git pull --rebase origin <分支>`，再重新推送

## 重要注意事项

- 创建公开仓库前，务必与用户确认（代码将公开可见）
- 未经用户明确要求，绝不使用强制推送（`--force`）到共享分支
- 未经用户同意，绝不修改 git 配置（user.name / user.email）
