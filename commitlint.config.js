module.exports = {
  extends: ["@commitlint/config-conventional"],
  rules: {
    "type-case": [2, "always", "start-case"],
    "type-enum": [
      2,
      "always",
      [
        "Init",
        "Feat",
        "Fix",
        "Update",
        "Remove",
        "Move",
        "Rename",
        "Docs",
        "Comment",
        "Refactor",
        "Test",
        "Chore",
      ],
    ],
  },
};
