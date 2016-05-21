package com.danielwestheide.kontextfrei.example

class LanguagesByPopularitySpec extends UnitSpec with LanguagesByPopularityProperties[Stream] {
  override val logic = new JobLogic[Stream]
}
