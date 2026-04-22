---
name: github-flow-assistant
description: >
  GitHub 工作流自动化助手。将代码变更经过"提交前检查 → 规范化 commit → 推送分支 → 开 PR"的完整流程对接 GitHub。
  当用户提到"提交"、"commit"、"push"、"推送"、"开 PR"、"发 PR"、"走 git 流程"、"交付到 GitHub"时自动激活，
  或在任务完成产出代码变更后主动提示是否走 Git 交付流程。
---

# GitHub Flow Assistant

GitHub 工作流自动化助手，完整流程：**提交前检查 → 规范化 commit → 推送分支 → 开 PR**。

## 触发条件

以下任一情况自动激活：
- Prompt 含关键词：提交、commit、push、推送、开 PR、发 PR、走 git 流程、交付到 GitHub
- 任务完成且产出了代码变更后，主动提示是否走 Git 交付

---

## 阶段 A：提交前检查（Pre-commit Guardrails）

**所有检查通过后才进入阶段 B，任一失败立即终止并报告。**

### A1. 运行编译
```bash
mvn clean compile -q
```
编译失败 → 终止，输出错误日志。

### A2. 运行单测
```bash
mvn test
```
测试失败 → 终止，输出失败用例。

### A3. 敏感内容扫描
扫描当前 `git diff --cached` 和 `git diff` 的变更内容，检查是否包含：
- `password`、`token`、`apikey`、`secret`、`private_key`（不区分大小写）

发现敏感内容 → 终止，列出文件和行号。

### A4. 敏感文件排除
检查变更文件列表，禁止提交以下文件：
- `.env`、`.pem`、`credentials.json`、`*private-key*`、`*.key`

发现敏感文件 → 终止，列出文件名。

### A5. 文件概览
列出 staged / unstaged 文件概览，供用户确认。

---

## 阶段 B：规范化 Commit

### B1. 暂存变更
```bash
git add -A
```
默认 add 所有非 `.gitignore` 的变更。

### B2. 生成 Commit Message

使用 **Conventional Commits** 格式：

```
<type>(<scope>): <subject>

<body - bullet list of main changes>

<footer - Refs/Closes/Fixes>
```

**type** 从变更性质判断：`feat` / `fix` / `refactor` / `test` / `docs` / `chore` / `perf`

**scope** 用模块名：`common` / `vehicle-service` / `user-service` / `vehicle-ui`

### B3. 用户确认
- 展示生成的 commit message
- 等待用户确认（y/n 或自定义修改）
- 确认后执行 `git commit`

---

## 阶段 C：推送 + 开 PR（需用户显式同意）

### C1. 分支检查
如果当前在 `main` / `master` 上：
- **强制警告**，建议新建 feature 分支
- 推荐命名：`feat/<scope>-<short-desc>` 或 `fix/<issue-num>-<short-desc>`
- 用户确认分支名后执行 `git checkout -b <branch-name>`

### C2. 推送
```bash
git push -u origin <current-branch>
```

### C3. 创建 PR
```bash
gh pr create \
  --title "<commit subject>" \
  --body "<PR Description>" \
  --base main \
  --head <current-branch>
```

PR Body 包含：
- 变更清单（bullet list）
- 测试覆盖说明
- 风险说明
- 关联 Issue（如有，加 `Fixes #N`）

### C4. 输出结果
输出 PR 链接，用户可点击查看。

---

## 安全边界（硬红线，永不执行）

- **禁止** `git push --force` / `-f`
- **禁止** `git reset --hard`
- **禁止** 对已推送 commit 执行 `--amend`
- **禁止** `gh pr merge`（不自动合并，保留人工 Review）
- **禁止** 删除分支
