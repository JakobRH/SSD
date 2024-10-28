(: Your XQuery goes here :)

<busyYearsForArtists>
{
let $years := //object/label/year/yearInteger
let $uniqueYears := distinct-values($years)
for $year in $uniqueYears
for $artist in //artist
let $objects := //object[label/year/yearInteger = $year and label/by = $artist/name]
where count($objects) > 3
order by $artist/lived/@dateOfBirth ascending
return
  <artist name="{$artist/name}" year="{$year}">
      <objectCount>{count($objects)}</objectCount>
      <object>{$objects[1]/title/text()}</object>
      <object>{$objects[2]/title/text()}</object>
      <object>{$objects[3]/title/text()}</object>
    </artist>
 }
</busyYearsForArtists>
