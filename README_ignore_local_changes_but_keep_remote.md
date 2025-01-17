# Ignore local changes but keep on remote

## 0.5 This is not `.gitignore`

## 1. View files in ignore list
Windows:
```
git ls-files -v | findstr "^S"
```

Linux:
```
git ls-files -v | grep ^S
```


## 2. Add a file to ignore list
```
git update-index --skip-worktree path/to/file
```

```
// Example:
git update-index --skip-worktree .idea/gradle.xml
```

## 3. Remove a file from ignore list
```
git update-index --no-skip-worktree path/to/file
```