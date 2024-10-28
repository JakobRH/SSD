<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:output method="html"/>
    <!-- Insert the XSLT Stylesheet here -->
    <xsl:template name="info">
        <xsl:param name="mode"> </xsl:param>
        <xsl:param name="tagname"> </xsl:param>

        <xsl:choose>
            <xsl:when test="$mode='artist'">
                <xsl:if test="//artist/tags/t = $tagname or (//object[tags/t = $tagname]/label/by = //artist/name)">
                    <td>
                        <table>
                            <tr><th><h3>Artists</h3></th></tr>
                            <xsl:for-each select="/art/artists/artist">
                                <xsl:sort select="/name" order="ascending"/>
                                <xsl:if test="./tags/t = $tagname or (//object[tags/t = $tagname]/label/by = ./name)">
                                    <tr><td><xsl:apply-templates select="."/></td></tr>
                                </xsl:if>
                            </xsl:for-each>
                            <td><b>Found: </b><xsl:value-of  select="count(//artist[tags/t = $tagname or name = //object[tags/t = $tagname]/label/by])"/> Artist(s)</td>
                        </table>
                    </td>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="//object/tags/t = $tagname or (//artist[tags/t = $tagname]/name = //object/label/by)">
                    <td>
                        <table>
                            <tr><th><h3>Exhibtions</h3></th></tr>
                            <xsl:for-each select="/art/objects/object">
                                <xsl:sort select="/label/year[1]" order="ascending"/>
                                <xsl:if test="./tags/t = $tagname or (//artist[tags/t = $tagname]/name = ./label/by)">
                                    <tr><td><xsl:apply-templates select="."/></td></tr>
                                </xsl:if>
                            </xsl:for-each>
                            <td><b>Found: </b><xsl:value-of  select="count(//object[tags/t = $tagname or label/by = //artist[tags/t = $tagname]/name])"/> Exhibition(s)</td>
                        </table>
                    </td>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>

    <xsl:template match="art">
        <html>
            <head>
                <title> Art - Tags Overview</title>
                <style>
body {
    font-family: Verdana, sans-serif;
}
table {
    font-family: Verdana, sans-serif;
    border-collapse: collapse;
    width: 31em;
}

td, th {
    border: 1px solid #080808;
    padding: 8px;    
    vertical-align: top;
}

tr:hover {background-color: #fdf3fd;}

th {
    padding-top: 2px;
    padding-bottom: 2px;
    text-align: center;
    background-color: #6c3b6c;
    color: white;
}
</style>
            </head>
            <body>
                <h1>Tags Overview</h1>
                  <hr/>
                <xsl:apply-templates select="//tag[@tagname]"/>
            </body>
        </html>
    </xsl:template>

	
	<!-- Insert additional templates here -->

    <xsl:template match="tag">
        <h2><xsl:value-of select="@tagname"/></h2>
        <b>Description:</b><br></br>
        <description>
            <xsl:choose>
                <xsl:when test=". != ''">
                    <xsl:value-of select="."/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>No further information on</xsl:text>>
                    <xsl:value-of select="@tagname"/>
                </xsl:otherwise>
            </xsl:choose>
        </description>
        <table>
            <tr>
                <xsl:call-template name="info">
                    <xsl:with-param name="mode">artist</xsl:with-param>
                    <xsl:with-param name="tagname" select="@tagname"/>
                </xsl:call-template>
                <xsl:call-template name="info">
                    <xsl:with-param name="mode">objects</xsl:with-param>
                    <xsl:with-param name="tagname" select="@tagname"/>
                </xsl:call-template>
            </tr>
        </table>
        <hr/>
    </xsl:template>
    <xsl:template match="artist">
        <xsl:value-of select="./name"/>
        <xsl:text>( </xsl:text>
        <xsl:value-of select="./lived/@dateOfBirth"/>
        <xsl:if test="./lived/@dateOfDeath">
            <xsl:text> to </xsl:text>
            <xsl:value-of select="./lived/@dateOfDeath"/>
        </xsl:if>
        <xsl:if test="./lived/@birthplace">
            <xsl:text>, born in </xsl:text>
            <xsl:value-of select="./lived/@birthplace"/>
        </xsl:if>
        <xsl:text> )</xsl:text>
    </xsl:template>
    <xsl:template match="object">
        <xsl:variable name="createdby" select="./label/by"/>
        <b>Title: </b>
        <xsl:value-of select="./title"/>
        <xsl:text>, </xsl:text>
        <b>Type: </b>
        <xsl:value-of select="upper-case(./kind/*/local-name())"/>
        <br/>
        <br/>
        <b>Label: </b><br></br>
        <i><xsl:value-of select="./label"/></i>
        <br/>
        <br/>
        <b>Created By: </b>
        <ul>
            <li>
                <xsl:choose>
                    <xsl:when test="./label/by = //artist/name">
                        <xsl:apply-templates select="//artist[name = $createdby]"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>Unregistered Artist</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </li>
        </ul>
    </xsl:template>
</xsl:stylesheet>


