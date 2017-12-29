package lu.mkremer.webprogsga.persistence;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;

@Entity
public class Tweed {

	private static final java.util.regex.Pattern MENTION_PATTERN = java.util.regex.Pattern.compile("[\\@|\\#][a-zA-Z0-9\\-\\_]+");

	@Id
	@Column(nullable=false)
	@Pattern(regexp="^#[a-zA-Z0-9\\-\\_]+$", message="Tweed name must only contain letters, numbers, hiphens and/or underscores")
	private String name;//TODO: The new primary key?
	
	@Column(nullable=false, length=200)
	private String content;
	
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date = Calendar.getInstance().getTime();
	
	@ManyToOne(optional=false)
	private User sender;
	
	@ManyToOne(optional=false)
	private Channel channel;
	
	public Tweed() {}
	
	public Tweed(String name, String content, User sender, Channel channel) {
		this.name = name;
		this.content = content;
		this.sender = sender;
		this.channel = channel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}
	
	public String getFormattedContent() {
		StringBuilder output = new StringBuilder();
		String rawContent = content
				.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("'", "&#39;")
				.replace("\"", "&quot;")
				.replace("&", "&amp;");
		Matcher matcher = MENTION_PATTERN.matcher(rawContent);
		int start = 0;
		while(matcher.find()) {
			String mention = matcher.group();
			output.append(rawContent.substring(start, matcher.start()));
			try {
				if(mention.startsWith("@")) {
					output.append("<a href=\"user.xhtml?id=" + URLEncoder.encode(mention.substring(1), "UTF-8") + "\">" + mention + "</a>");
				}else if(mention.startsWith("#")) {
					output.append("<strong>" + mention + "</strong>");//TODO: Redirect to tweed
				}else {
					output.append(mention);
				}
			}catch(UnsupportedEncodingException e) {
				e.printStackTrace();
				output.append(mention);
			}
			start = matcher.end();
		}
		if(start < rawContent.length()) {
			output.append(rawContent.substring(start, rawContent.length()));
		}
		return output.toString();
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
}
