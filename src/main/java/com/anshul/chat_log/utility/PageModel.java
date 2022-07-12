package com.anshul.chat_log.utility;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageModel<T> {

	private List<T> items;
    private long from;
    private long to;
    private long count;
    private long size;
    
}
