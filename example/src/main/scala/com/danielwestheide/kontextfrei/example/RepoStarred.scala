package com.danielwestheide.kontextfrei.example

import org.joda.time.DateTime

case class RepoStarred(
  at: DateTime,
  owner: String,
  repo: String,
  starrerId: String
)
