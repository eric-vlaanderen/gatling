/**
 * Copyright 2011-2012 eBusiness Information, Groupe Excilys (www.excilys.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.excilys.ebi.gatling.http.check.body

import com.excilys.ebi.gatling.core.check.CheckContext.getOrUpdateCheckContextAttribute
import com.excilys.ebi.gatling.core.check.extractor.regex.RegexExtractor
import com.excilys.ebi.gatling.core.check.ExtractorFactory
import com.excilys.ebi.gatling.core.session.EvaluatableString
import com.ning.http.client.Response

object HttpBodyRegexCheckBuilder {

	def regex(expression: EvaluatableString) = new HttpBodyCheckBuilder(findExtractorFactory, findAllExtractorFactory, countExtractorFactory, expression)

	private val HTTP_BODY_REGEX_EXTRACTOR_CONTEXT_KEY = "HttpBodyRegexExtractor"

	private def getCachedExtractor(response: Response) = getOrUpdateCheckContextAttribute(HTTP_BODY_REGEX_EXTRACTOR_CONTEXT_KEY, new RegexExtractor(response.getResponseBody))

	private def findExtractorFactory(occurrence: Int): ExtractorFactory[Response, String] = (response: Response) => getCachedExtractor(response).extractOne(occurrence)

	private val findAllExtractorFactory: ExtractorFactory[Response, Seq[String]] = (response: Response) => getCachedExtractor(response).extractMultiple

	private val countExtractorFactory: ExtractorFactory[Response, Int] = (response: Response) => getCachedExtractor(response).count
}