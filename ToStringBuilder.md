# [ToStringBuilder](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/build/tostring/ToStringBuilder.java)

There are plenty of open source ToString helpers, like those present in Guava and Apache Commons Lang. 
However they are fairly simple and can be reduced to appending labels and values in pairs. While functional, this can 
be improved, to help make the String a little more helpful for logs and debugging purposes.

## Useful supported features

- Pick the fields/properties that should appear in the final toString.
- Optional skip falsey properties (nulls, zeros)
- Escape AND/OR quote char like types such as `char[]` & `CharSequence`. Useful to assist reading strings with escaped chars such as CR, TABS.
- Unwrapping values present `Optional` and apply other formatting such as quoting, escaping etc.
- Hex encoding byte and other numeric values. May be helpful with byte arrays.
- Support for nested objects each supporting tuning via `ToStringBuilderOption`
- Hard limits on value and length
- Auto delimiters between elements, entries etc.

Many of these can be enabled or disabled via [ToStringBuilderOption](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/build/tostring/ToStringBuilderOption.java)

## Url value type sample

This is a very simple example using a Url record like class. Our record would have the following properties which would
be populated after parsing a URL String into its core components.

- scheme (http OR https)
- host
- port
- path
- params as a Map
- anchor

Many of the above properties will be `null` or empty string, the goal of `ToStringBuilder` is that it facilitates
rebuilding something that almost looks url, which means skipping properties that are not present.

- scheme "https://"
- host "example.com"
- params a=b c=d

```java
ToStringBuilder.create()
  .disable(ToStringBuilderOption.QUOTE) // added for clarity, auto included one of many sensible defaults exist.
  .disable(ToStringBuilderOption.ESCAPE)
  .separator("") // appear between value(XXX) calls
  .value(this.scheme)
  .value(this.host)
  .label(":")
  .value(this.port)
  .value(this.path)
  
   // tries to reproduce key/values joined by '&' but without URL encoding.
  .labelSeparator("=").valueSeparator("&").value(this.params)
  
  // will conditionally add the '#' if anchor is not empty(falsey) 
  .label("#").value(this.anchor)
  .build()
  
// gives ... https://example.com/?a=b&c=d 
```

Its not perfect, but it does make for more beautiful and readable strings, with very little effort.

- [unit Tests](https://github.com/mP1/walkingkooka/blob/master/src/test/java/walkingkooka/build/tostring)